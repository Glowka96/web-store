package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.buildhelpers.ProductBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.PromotionPriceGreaterThanBasePriceException;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProductPricePromotionMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;
import com.example.portfolio.webstorespring.repositories.products.ProductPricePromotionRepository;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.ProductPricePromotionBuilderHelper.createProductPricePromotion;
import static com.example.portfolio.webstorespring.buildhelpers.ProductPricePromotionBuilderHelper.createProductPricePromotionRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductPricePromotionServiceTest {

    @Mock
    private ProductPricePromotionRepository promotionRepository;
    @Mock
    private ProductRepository productRepository;
    @Spy
    private ProductPricePromotionMapper promotionMapper = Mappers.getMapper(ProductPricePromotionMapper.class);
    @InjectMocks
    private ProductPricePromotionService underTest;

    @Test
    void shouldSaveProductPricePromotion() {
        // given
        Product product = ProductBuilderHelper.createProduct();
        ProductPricePromotionRequest productPricePromotionRequest = createProductPricePromotionRequest();

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        ProductPricePromotionResponse savedProductPricePromotionRequest = underTest.saveProductPricePromotion(productPricePromotionRequest);

        // then
        ArgumentCaptor<ProductPricePromotion> productPricePromotionArgumentCaptor =
                ArgumentCaptor.forClass(ProductPricePromotion.class);
        verify(promotionRepository).save(productPricePromotionArgumentCaptor.capture());

        ProductPricePromotion promotion = productPricePromotionArgumentCaptor.getValue();
        ProductPricePromotionResponse mappedProductPricePromotionResponse = promotionMapper.mapToDto(promotion);

        assertThat(mappedProductPricePromotionResponse).isEqualTo(savedProductPricePromotionRequest);
    }

    @Test
    void willThrowWhenPromotionPriceIsGreaterThanBasePrice() {
        // given
        Product product = ProductBuilderHelper.createProduct();
        ProductPricePromotionRequest productPricePromotionRequest = createProductPricePromotionRequest(BigDecimal.valueOf(999.99));

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        // then
        assertThrows(PromotionPriceGreaterThanBasePriceException.class, () -> underTest.saveProductPricePromotion(productPricePromotionRequest));
        verify(productRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(promotionRepository);
    }

    @Test
    void shouldDeleteProductPricePromotionById() {
        // given
        ProductPricePromotion promotion = createProductPricePromotion();

        given(promotionRepository.findById(anyLong())).willReturn(Optional.of(promotion));

        // when
        underTest.deleteProductPricePromotionById(1L);

        // then
        verify(promotionRepository, times(1)).findById(1L);
        verify(promotionRepository, times(1)).delete(promotion);
        verifyNoMoreInteractions(promotionRepository);
    }

    @Test
    void willThrowWhenProductPricePromotionNotFound() {
        // given
        // when
        // then
        assertThatThrownBy(() -> underTest.deleteProductPricePromotionById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ProductPricePromotion with id 1 not found");
    }
}
