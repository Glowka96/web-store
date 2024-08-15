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
public class ProductsPageService {

    private final ProductRepository productRepository;
    private final Clock clock = Clock.systemUTC();

    @Transactional(readOnly = true)
    public PageProductsWithPromotionDTO getPageProductsBySubcategoryId(Long subcategoryId, PageProductsOptions pageProductsOptions) {
        return getPageProduct(
                pageProductsOptions,
                pageable -> getProductsBySubcategoryId(subcategoryId, getLocalDateTime30DaysAgo(), pageable)
        );
    }

    @Transactional(readOnly = true)
    public PageProductsWithPromotionDTO getPageSearchProducts(String text, PageProductsOptions pageProductsOptions) {
        return getPageProduct(
                pageProductsOptions,
                pageable -> searchProductsByText(text, getLocalDateTime30DaysAgo(), pageable)
        );
    }

    @Transactional(readOnly = true)
    public PageProductsWithPromotionDTO getPagePromotionProduct(PageProductsOptions pageProductsOptions) {
        return getPageProduct(
                pageProductsOptions,
                pageable -> getPromotionProducts(getLocalDateTime30DaysAgo(), pageable)
        );
    }

    @Transactional(readOnly = true)
    public PageProductsWithPromotionDTO getPageNewProduct(PageProductsOptions pageProductsOptions) {
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
        String sortType = getSortTypeName(sortTypeAndDirection);
        return PageRequest.of(pageProductsOptions.pageNo(),
                pageProductsOptions.size(),
                Sort.by(Sort.Direction.fromString(sortTypeAndDirection[1]), sortType)
        );
    }

    @NotNull
    private String getSortTypeName(String[] sortTypeAndDirection) {
        return Arrays.stream(SortByType.values())
                .filter(sortByType -> sortByType.name().equalsIgnoreCase(sortTypeAndDirection[0]))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid sort type value: %s", sortTypeAndDirection[0])))
                .getFieldName();
    }

    @NotNull
    private static List<String> getSortOptions() {
        return Arrays.stream(SortByType.values())
                .flatMap(s -> Arrays.stream(SortDirectionType.values())
                        .map(d -> s.getFieldName() + " - " + d.name().toLowerCase()))
                .toList();
    }

    @NotNull
    private LocalDateTime getLocalDateTime30DaysAgo() {
        return LocalDateTime.now(clock).minusDays(30);
    }
}
