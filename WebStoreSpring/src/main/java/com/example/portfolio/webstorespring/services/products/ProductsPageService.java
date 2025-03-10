package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.models.dtos.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.models.dtos.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.models.dtos.products.ProductsPageOptions;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductsPageService {

    private final ProductRepository productRepository;
    private final Clock clock;

    public PageProductsWithPromotionDTO getProductsPageBySubcategoryId(Long subcategoryId, ProductsPageOptions productsPageOptions) {
        return getProductsPage(
                productsPageOptions,
                pageable -> getProductsBySubcategoryId(subcategoryId, pageable)
        );
    }

    public PageProductsWithPromotionDTO getSearchProductsPage(String text, ProductsPageOptions productsPageOptions) {
        return getProductsPage(
                productsPageOptions,
                pageable -> searchProductsByText(text, pageable)
        );
    }

    public PageProductsWithPromotionDTO getPromotionProductsPage(ProductsPageOptions productsPageOptions) {
        return getProductsPage(
                productsPageOptions,
                this::getPromotionProducts
        );
    }

    public PageProductsWithPromotionDTO getNewProductsPage(ProductsPageOptions productsPageOptions) {
        return getProductsPage(
                productsPageOptions,
                this::getNewProducts
        );
    }

    private PageProductsWithPromotionDTO getProductsPage(ProductsPageOptions productsPageOptions,
                                                         Function<Pageable, Page<ProductWithPromotionDTO>> function) {
        log.info("Fetching product for product page options: {}", productsPageOptions);
        Pageable pageable = getPageable(productsPageOptions);
        Page<ProductWithPromotionDTO> productPage = function.apply(pageable);

        log.debug("Getting sort options.");
        List<String> sortOptions = getSortOptions();
        log.info("Returning page.");
        return new PageProductsWithPromotionDTO(
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                sortOptions,
                productPage.getContent());
    }

    private Page<ProductWithPromotionDTO> getProductsBySubcategoryId(Long subcategoryId,
                                                                     Pageable pageable) {
        return productRepository.findBySubcategoryId(subcategoryId, getLocalDateTime30DaysAgo(), pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> searchProductsByText(String text,
                                                               Pageable pageable) {
        return productRepository.searchByEnteredText(text, getLocalDateTime30DaysAgo(), pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> getPromotionProducts(Pageable pageable) {
        return productRepository.findPromotionProducts(getLocalDateTime30DaysAgo(), pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> getNewProducts(Pageable pageable) {
        return productRepository.findNewProducts(getLocalDateTime30DaysAgo(), pageable)
                .orElse(Page.empty());
    }

    @NotNull
    private Pageable getPageable(ProductsPageOptions productsPageOptions) {
        log.debug("Separating sortType and sortDirection.");
        String[] sortTypeAndDirection = productsPageOptions.sortOption().split("\\s-\\s");
        String sortType = SortByType.findFieldNameOfSortByType(sortTypeAndDirection[0]);
        log.debug("Returning pageable.");
        return PageRequest.of(
                productsPageOptions.pageNo(),
                productsPageOptions.size(),
                Sort.by(Sort.Direction.fromString(sortTypeAndDirection[1]), sortType)
        );
    }

    @NotNull
    private static List<String> getSortOptions() {
        return Arrays.stream(SortByType.values())
                .flatMap(s -> Arrays.stream(Sort.Direction.values())
                        .map(d -> s.toString().toLowerCase() + " - " + d.name().toLowerCase()))
                .toList();
    }

    @NotNull
    private LocalDateTime getLocalDateTime30DaysAgo() {
        return LocalDateTime.now(clock).minusDays(30);
    }
}
