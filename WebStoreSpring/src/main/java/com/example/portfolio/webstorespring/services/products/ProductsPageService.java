package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.enums.SortDirectionType;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsOptions;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import lombok.RequiredArgsConstructor;
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
public class ProductsPageService {

    private final ProductRepository productRepository;
    private final Clock clock = Clock.systemUTC();

    public PageProductsWithPromotionDTO getPageProductsBySubcategoryId(Long subcategoryId, PageProductsOptions pageProductsOptions) {
        return getPageProduct(
                pageProductsOptions,
                pageable -> getProductsBySubcategoryId(subcategoryId, getLocalDateTime30DaysAgo(), pageable)
        );
    }

    public PageProductsWithPromotionDTO getPageSearchProducts(String text, PageProductsOptions pageProductsOptions) {
        return getPageProduct(
                pageProductsOptions,
                pageable -> searchProductsByText(text, getLocalDateTime30DaysAgo(), pageable)
        );
    }

    public PageProductsWithPromotionDTO getPagePromotionProduct(PageProductsOptions pageProductsOptions) {
        return getPageProduct(
                pageProductsOptions,
                pageable -> getPromotionProducts(getLocalDateTime30DaysAgo(), pageable)
        );
    }

    public PageProductsWithPromotionDTO getPageNewProducts(PageProductsOptions pageProductsOptions) {
        return getPageProduct(
                pageProductsOptions,
                pageable -> getNewProducts(getLocalDateTime30DaysAgo(), pageable)
        );
    }

    private PageProductsWithPromotionDTO getPageProduct(PageProductsOptions pageProductsOptions,
                                                        Function<Pageable, Page<ProductWithPromotionDTO>> function) {
        Pageable pageable = getPageable(pageProductsOptions);
        Page<ProductWithPromotionDTO> productPage = function.apply(pageable);

        List<String> sortOptions = getSortOptions();
        return new PageProductsWithPromotionDTO(
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                sortOptions,
                productPage.getContent());
    }

    private Page<ProductWithPromotionDTO> getProductsBySubcategoryId(Long subcategoryId,
                                                                     LocalDateTime date30DaysAgo,
                                                                     Pageable pageable) {
        return productRepository.findProductsBySubcategoryId(subcategoryId, date30DaysAgo, pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> searchProductsByText(String text,
                                                               LocalDateTime date30DaysAgo,
                                                               Pageable pageable) {
        return productRepository.searchProductsByEnteredText(text, date30DaysAgo, pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> getPromotionProducts(LocalDateTime date30DaysAgo, Pageable pageable) {
        return productRepository.findPromotionProducts(date30DaysAgo, pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> getNewProducts(LocalDateTime date30DaysAgo, Pageable pageable) {
        return productRepository.findNewProducts(date30DaysAgo, pageable)
                .orElse(Page.empty());
    }

    @NotNull
    private Pageable getPageable(PageProductsOptions pageProductsOptions) {
        String[] sortTypeAndDirection = pageProductsOptions.sortOption().split("\\s-\\s");
        String sortType = SortByType.findFieldNameOfSortByType(sortTypeAndDirection[0]);
        return PageRequest.of(pageProductsOptions.pageNo(),
                pageProductsOptions.size(),
                Sort.by(Sort.Direction.fromString(sortTypeAndDirection[1]), sortType)
        );
    }

    @NotNull
    private static List<String> getSortOptions() {
        return Arrays.stream(SortByType.values())
                .flatMap(s -> Arrays.stream(SortDirectionType.values())
                        .map(d -> s.toString().toLowerCase() + " - " + d.name().toLowerCase()))
                .toList();
    }

    @NotNull
    private LocalDateTime getLocalDateTime30DaysAgo() {
        return LocalDateTime.now(clock).minusDays(30);
    }
}
