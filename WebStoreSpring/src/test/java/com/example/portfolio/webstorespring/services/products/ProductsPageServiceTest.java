package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.enums.SortDirectionType;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithPromotionDtoBuildHelper.createProductWithPromotionDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductsPageServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductsPageService underTest;

    @Test
    void shouldGetPageProductsBySubCategoryId() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name"));
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageProductsBySubcategoryId(1L, 0, 5, SortByType.NAME, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }


    @Test
    void shouldGetPageNewProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findNewProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageNewProduct(0, 5, SortByType.NAME, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetEmptyPageWhenNoHaveNewProduct() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name"));
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        given(productRepository.findNewProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageNewProduct(0, 5, SortByType.NAME, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).isEmpty();
        assertThat(actual.totalPages()).isZero();
        assertThat(actual.totalElements()).isZero();
    }

    @Test
    void shouldPageProductsBySubCategoryIdWhenGetSubCategoryId_PageNo_PageSize_SortBy_SortDirection() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name"));
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findProductsBySubcategory_Id(anyLong(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageProductsBySubcategoryId(1L, 0, 5, SortByType.NAME, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPageProductsBySearchText() {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name"));
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.searchProductsByEnteredText(anyString(), any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPageSearchProducts("test", 0, 5, SortByType.NAME, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldGetPromotionProducts() {
        // given
        Pageable pageable= PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name"));
        List<ProductWithPromotionDTO> productList = getListProductWithPromotionDto();
        Page<ProductWithPromotionDTO> productPage = new PageImpl<>(productList, pageable, productList.size());

        given(productRepository.findPromotionProducts(any(), any())).willReturn(Optional.of(productPage));

        // when
        PageProductsWithPromotionDTO actual = underTest.getPagePromotionProduct(0, 5, SortByType.NAME, SortDirectionType.ASC);

        // then
        assertThat(actual.products()).hasSize(productList.size());
        assertThat(actual.totalPages()).isEqualTo(1);
        assertThat(actual.totalElements()).isEqualTo(3);
    }


    private List<ProductWithPromotionDTO> getListProductWithPromotionDto() {
        ProductWithPromotionDTO productWithPromotionDTO = createProductWithPromotionDTO();
        return List.of(productWithPromotionDTO, productWithPromotionDTO, productWithPromotionDTO);
    }

//    @NotNull
//    private ProductWithPromotionDTO getProductDTO() {
//        when(clock.getZone()).thenReturn(zonedDateTime.getZone());
//        when(clock.instant()).thenReturn(zonedDateTime.toInstant());
//        return createProductWithPromotionDTO(clock);
//    }
}
