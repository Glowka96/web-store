package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.enums.SortDirectionType;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionAndLowestPriceDTO;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductPageServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private Clock clock;
    @InjectMocks
    private ProductPageService underTest;

    private final ZonedDateTime zonedDateTime = ZonedDateTime.of(
            2023,
            3,
            9,
            12,
            30,
            30,
            0,
            ZoneId.of("GMT")
    );

    @Test
    void shouldGetPageProductsBySubCategoryId() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageProductsBySubcategoryId(1L, 0, 5, SortByType.ID, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPageProductsBySubCategoryIdReturnTwoTotalPages() {
        // given
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageProductsBySubcategoryId(1L, 0, 2, SortByType.ID, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(2);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPageNewProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findNewProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageNewProduct(0, 5, SortByType.ID, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetEmptyPageWhenNoHaveNewProduct() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        given(productRepository.findNewProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageNewProduct(0, 5, SortByType.ID, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).isEmpty();
        assertThat(actual.totalPages()).isZero();
        assertThat(actual.totalElements()).isZero();
    }

    @Test
    void shouldPageProductsBySubCategoryIdWhenGetSubCategoryId_PageNo_PageSize_SortBy_SortDirection() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageProductsBySubcategoryId(1L, 0, 5, SortByType.ID, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPageProductsBySearchText() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.searchProductsByEnteredText(anyString(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageSearchProducts("test", 0, 5, SortByType.ID, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPromotionProducts() {
        // given
        Pageable pageable= PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionAndLowestPriceDTO> productList = List.of(getProductDTO(), getProductDTO(), getProductDTO());
        Page<ProductWithPromotionAndLowestPriceDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findPromotionProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPagePromotionProduct(0, 5, SortByType.ID, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }


    @NotNull
    private ProductWithPromotionAndLowestPriceDTO getProductDTO() {
        when(clock.getZone()).thenReturn(zonedDateTime.getZone());
        when(clock.instant()).thenReturn(zonedDateTime.toInstant());
        return new ProductWithPromotionAndLowestPriceDTO(
                1L,
                "Test",
                "test.pl/test.png",
                1L, "test",
                BigDecimal.valueOf(100L),
                BigDecimal.valueOf(90L),
                BigDecimal.valueOf(70L),
                Date.from(LocalDateTime.now(clock).plusDays(15).atZone(ZoneId.systemDefault()).toInstant())
        );
    }

}
