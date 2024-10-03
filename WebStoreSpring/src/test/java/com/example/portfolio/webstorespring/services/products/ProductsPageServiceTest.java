package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithPromotionDtoBuildHelper.createProductWithPromotionDTO;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductsPageOptionsBuilderHelper.createBaseProductPageOptions;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductsPageOptionsBuilderHelper.getNumberOfSortOptions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductsPageServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductsPageService underTest;
    private static Integer numberOfSortOptions;

    @BeforeAll
    static void initNumberOfSorOptions() {
        numberOfSortOptions = getNumberOfSortOptions();
    }

    @ParameterizedTest
    @CsvSource({
            "name, asc, name, asc",
            "price, desc, price, desc",
            "type, desc, type, desc",
            "date, asc, createdAt, asc"
    })
    void shouldGetProductsPageBySubcategoryId(String sortType,
                                              String sortDirection,
                                              String expectedSortType,
                                              String expectedSortDirection) {
        Pageable pageable = PageRequest.of(0,
                5,
                Sort.by(Sort.Direction.fromString(expectedSortDirection), expectedSortType)
        );
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategoryId(anyLong(), any(), any()))
                .willReturn(Optional.of(productPage));

        PageProductsWithPromotionDTO actual = underTest.getProductsPageBySubcategoryId(
                1L, createBaseProductPageOptions(sortType, sortDirection)
        );

        assertProductsPageWithPromotion(actual, productList);
    }


    @ParameterizedTest
    @CsvSource({
            "name, asc, name, asc",
            "price, desc, price, desc",
            "type, desc, type, desc",
            "date, asc, createdAt, asc"
    })
    void shouldGetNewProductsPage(String sortType,
                                  String sortDirection,
                                  String expectedSortType,
                                  String expectedSortDirection) {
        Pageable pageable = PageRequest.of(0,
                5,
                Sort.by(Sort.Direction.fromString(expectedSortDirection), expectedSortType)
        );
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findNewProducts(any(), any())).willReturn(Optional.of(productPage));

        PageProductsWithPromotionDTO actual = underTest.getNewProductsPage(
                createBaseProductPageOptions(sortType, sortDirection)
        );

        assertProductsPageWithPromotion(actual, productList);
    }

    @Test
    void shouldGetEmptyPage_whenNoHaveNewProduct() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name"));
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        given(productRepository.findNewProducts(any(), any())).willReturn(Optional.of(productPage));

        PageProductsWithPromotionDTO actual = underTest.getNewProductsPage(createBaseProductPageOptions());

        assertThat(actual.products()).isEmpty();
        assertThat(actual.totalPages()).isZero();
        assertThat(actual.totalElements()).isZero();
    }

    @ParameterizedTest
    @CsvSource({
            "name, asc, name, asc",
            "price, desc, price, desc",
            "type, desc, type, desc",
            "date, asc, createdAt, asc"
    })
    void shouldGetPageProductsBySearchText(String sortType, String sortDirection, String expectedSortType, String expectedSortDirection) {
        Pageable pageable = PageRequest.of(0,
                5,
                Sort.by(Sort.Direction.fromString(expectedSortDirection), expectedSortType));
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.searchProductsByEnteredText(anyString(), any(), any()))
                .willReturn(Optional.of(productPage));

        PageProductsWithPromotionDTO actual = underTest.getSearchProductsPage(
                "test", createBaseProductPageOptions(sortType, sortDirection)
        );

        assertProductsPageWithPromotion(actual, productList);
    }

    @ParameterizedTest
    @CsvSource({
            "name, asc, name, asc",
            "price, desc, price, desc",
            "type, desc, type, desc",
            "date, asc, createdAt, asc"
    })
    void shouldGetPromotionProductsPage(String sortType,
                                    String sortDirection,
                                    String expectedSortType,
                                    String expectedSortDirection) {
        Pageable pageable = PageRequest.of(0,
                5,
                Sort.by(Sort.Direction.fromString(expectedSortDirection), expectedSortType)
        );
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findPromotionProducts(any(), any())).willReturn(Optional.of(productPage));

        PageProductsWithPromotionDTO actual = underTest.getPromotionProductsPage(
                createBaseProductPageOptions(sortType, sortDirection)
        );

        assertProductsPageWithPromotion(actual, productList);
    }

    private static void assertProductsPageWithPromotion(PageProductsWithPromotionDTO actual, List<ProductWithPromotionDTO> productList) {
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
        assertThat(actual.sortOptions()).hasSize(numberOfSortOptions);
    }

    @ParameterizedTest
    @CsvSource({
            "bad, asc",
            "bad, desc"
    })
    void willThrowIllegalArgumentException_whenSortTypeIsInvalid(String sortType, String sortDirection) {
        assertThatThrownBy(() -> underTest.getNewProductsPage(createBaseProductPageOptions(sortType, sortDirection)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid sort type value: bad");
    }


    @ParameterizedTest
    @CsvSource({
            "name, bad",
            "price, bad",
            "type, bad",
            "date, bad"
    })
    void willThrowIllegalArgumentException_whenSortDirectionIsInvalid(String sortType, String sortDirection) {
        assertThatThrownBy(() -> underTest.getNewProductsPage(createBaseProductPageOptions(sortType, sortDirection)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid value 'bad' for orders given");
    }

    private List<ProductWithPromotionDTO> getListProductWithPromotionDto() {
        ProductWithPromotionDTO productWithPromotionDTO = createProductWithPromotionDTO();
        return List.of(productWithPromotionDTO, productWithPromotionDTO, productWithPromotionDTO);
    }
}
