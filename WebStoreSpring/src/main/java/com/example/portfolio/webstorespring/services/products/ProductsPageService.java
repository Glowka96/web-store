package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.enums.SortDirectionType;
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
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductsPageService {

    private final ProductRepository productRepository;
    private final Clock clock = Clock.systemUTC();

    @Transactional(readOnly = true)
    public PageProductsWithPromotionDTO getPageProductsBySubcategoryId(Long subcategoryId,
                                                                       Integer pageNo,
                                                                       Integer pageSize,
                                                                       SortByType sortBy,
                                                                       SortDirectionType sortDirection) {
        return getPageProduct(
                pageNo, pageSize, sortBy, sortDirection,
                pageable -> getProductsBySubcategoryId(subcategoryId, getDate30DaysAgo(), pageable)
        );
    }

    @Transactional(readOnly = true)
    public PageProductsWithPromotionDTO getPageSearchProducts(String text,
                                                              Integer pageNo,
                                                              Integer pageSize,
                                                              SortByType sortBy,
                                                              SortDirectionType sortDirection) {
        return getPageProduct(
                pageNo, pageSize, sortBy, sortDirection,
                pageable -> searchProductsByText(text, getDate30DaysAgo(), pageable)
        );
    }

    @Transactional(readOnly = true)
    public PageProductsWithPromotionDTO getPagePromotionProduct(Integer pageNo,
                                                                Integer pageSize,
                                                                SortByType sortBy,
                                                                SortDirectionType sortDirection) {
        return getPageProduct(
                pageNo, pageSize, sortBy, sortDirection,
                pageable -> getNewProducts(getDate30DaysAgo(), pageable)
        );
    }

    @Transactional(readOnly = true)
    public PageProductsWithPromotionDTO getPageNewProduct(Integer pageNo,
                                                          Integer pageSize,
                                                          SortByType sortBy,
                                                          SortDirectionType sortDirection) {
        return getPageProduct(
                pageNo, pageSize, sortBy, sortDirection,
                pageable -> getNewProducts(getDate30DaysAgo(), pageable)
                );
    }

    private PageProductsWithPromotionDTO getPageProduct(Integer pageNo,
                                                        Integer pageSize,
                                                        SortByType sortBy,
                                                        SortDirectionType sortDirection,
                                                        Function<Pageable, Page<ProductWithPromotionDTO>> function) {
        Pageable pageable = createPageable(pageNo, pageSize, sortBy.getFieldName(), sortDirection.name());
        Page<ProductWithPromotionDTO> productPage = function.apply(pageable);

        List<String> sortByTypes = getEnumTypes(SortByType.class);
        List<String> sortDirectionTypes = getEnumTypes(SortDirectionType.class);
        return new PageProductsWithPromotionDTO(
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                sortByTypes,
                sortDirectionTypes,
                productPage.getContent());
    }

    @NotNull
    private Date getDate30DaysAgo() {
        return Date.from(LocalDateTime.now(clock).minusDays(30)
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    private Pageable createPageable(Integer pageNo, Integer pageSize, String sortBy, String sortDirection) {
        return PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
    }

    private Page<ProductWithPromotionDTO> getProductsBySubcategoryId(Long subcategoryId,
                                                                     Date date30DaysAgo,
                                                                     Pageable pageable) {
        return productRepository.findProductsBySubcategory_Id(subcategoryId, date30DaysAgo, pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> searchProductsByText(String text,
                                                               Date date30DaysAgo,
                                                               Pageable pageable) {
        return productRepository.searchProductsByEnteredText(text, date30DaysAgo, pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> getPromotionProducts(Date date30DaysAgo, Pageable pageable) {
        return productRepository.findPromotionProducts(date30DaysAgo, pageable)
                .orElse(Page.empty());
    }

    private Page<ProductWithPromotionDTO> getNewProducts(Date date30DaysAgo, Pageable pageable) {
        return productRepository.findNewProducts(date30DaysAgo, pageable)
                .orElse(Page.empty());
    }

    @NotNull
    private static <E extends Enum<E>> List<String> getEnumTypes(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map((sortByType -> sortByType.name().toLowerCase()))
                .toList();
    }
}
