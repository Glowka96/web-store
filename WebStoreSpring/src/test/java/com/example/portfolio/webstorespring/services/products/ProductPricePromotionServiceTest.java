package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ProductHasAlreadyPromotionException;
import com.example.portfolio.webstorespring.exceptions.PromotionPriceGreaterThanBasePriceException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.mappers.ProductPricePromotionMapper;
import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;
import com.example.portfolio.webstorespring.repositories.products.ProductPricePromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.BASIC_PRODUCT;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.PRICE_PROMOTIONS;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.BASIC_PROMOTION;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductPricePromotionBuilderHelper.createProductPricePromotionRequest;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ProductPricePromotionServiceTest {

    @Mock
    private ProductPricePromotionRepository promotionRepository;
    @Mock
    private ProductService productService;
    @Spy
    private ProductPricePromotionMapper promotionMapper = Mappers.getMapper(ProductPricePromotionMapper.class);
    @InjectMocks
    private ProductPricePromotionService underTest;

    @Test
    void shouldSaveProductPricePromotion() {
        setMappers();

        Product product = make(a(BASIC_PRODUCT).but(with(PRICE_PROMOTIONS, Set.of())));
        ProductPricePromotionRequest productPricePromotionRequest = createProductPricePromotionRequest();

        given(productService.findProductByIdWithPromotion(anyLong())).willReturn(product);

        ProductPricePromotionResponse savedProductPricePromotionRequest =
                underTest.saveProductPricePromotion(productPricePromotionRequest);

        ArgumentCaptor<ProductPricePromotion> productPricePromotionArgumentCaptor =
                ArgumentCaptor.forClass(ProductPricePromotion.class);
        verify(promotionRepository).save(productPricePromotionArgumentCaptor.capture());

        ProductPricePromotionResponse mappedProductPricePromotionResponse =
                promotionMapper.mapToDto(productPricePromotionArgumentCaptor.getValue());

        assertEquals(mappedProductPricePromotionResponse, savedProductPricePromotionRequest);
    }

    private void setMappers() {
        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        ReflectionTestUtils.setField(promotionMapper, "productMapper", productMapper);
        ProductTypeMapper productTypeMapper = Mappers.getMapper(ProductTypeMapper.class);
        ReflectionTestUtils.setField(productMapper, "productTypeMapper", productTypeMapper);
        ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
        ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);
    }

    @Test
    void willThrowPromotionPriceGreaterThanBasePriceException() {
        Product product = make(a(BASIC_PRODUCT).but(with(PRICE_PROMOTIONS, Set.of())));
        ProductPricePromotionRequest productPricePromotionRequest = createProductPricePromotionRequest(BigDecimal.valueOf(999.99));

        when(productService.findProductByIdWithPromotion(anyLong())).thenReturn(product);

        assertThrows(PromotionPriceGreaterThanBasePriceException.class, () -> underTest.saveProductPricePromotion(productPricePromotionRequest));
        verify(productService, times(1)).findProductByIdWithPromotion(1L);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(promotionRepository);
    }

    @Test
    void willThrowWhenProductHasAlreadyPromotion() {
        ProductPricePromotion productPricePromotion = make(a(BASIC_PROMOTION));
        Product product = make(a(BASIC_PRODUCT).but(with(PRICE_PROMOTIONS, Set.of(productPricePromotion))));

        ProductPricePromotionRequest productPricePromotionRequest = createProductPricePromotionRequest();

        when(productService.findProductByIdWithPromotion(anyLong())).thenReturn(product);

        assertThrows(ProductHasAlreadyPromotionException.class, () -> underTest.saveProductPricePromotion(productPricePromotionRequest));
        verify(productService, times(1)).findProductByIdWithPromotion(1L);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(promotionRepository);
    }
}
