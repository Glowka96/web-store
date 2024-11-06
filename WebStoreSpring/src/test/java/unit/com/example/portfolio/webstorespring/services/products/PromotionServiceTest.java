package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.buildhelpers.products.PromotionBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ProductHasAlreadyPromotionException;
import com.example.portfolio.webstorespring.exceptions.PromotionPriceGreaterThanBasePriceException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.mappers.ProductMapper;
import com.example.portfolio.webstorespring.mappers.ProductTypeMapper;
import com.example.portfolio.webstorespring.mappers.PromotionMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.PromotionRequesst;
import com.example.portfolio.webstorespring.model.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.Promotion;
import com.example.portfolio.webstorespring.repositories.products.PromotionRepository;
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
import static com.example.portfolio.webstorespring.buildhelpers.products.PromotionBuilderHelper.BASIC_PROMOTION;
import static com.example.portfolio.webstorespring.buildhelpers.products.PromotionBuilderHelper.createPromotionRequest;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class PromotionServiceTest {

    @Mock
    private PromotionRepository promotionRepository;
    @Mock
    private ProductService productService;
    @Spy
    private PromotionMapper promotionMapper = Mappers.getMapper(PromotionMapper.class);
    @InjectMocks
    private PromotionService underTest;

    @Test
    void shouldSavePromotion() {
        setMappers();

        Product product = make(a(BASIC_PRODUCT).but(with(PRICE_PROMOTIONS, Set.of())));
        PromotionRequesst promotionRequesst = PromotionBuilderHelper.createPromotionRequest();

        given(productService.findProductByIdWithPromotion(anyLong())).willReturn(product);

        PromotionResponse savedPromotionRequest =
                underTest.savePromotion(promotionRequesst);

        ArgumentCaptor<Promotion> promotionArgumentCaptor =
                ArgumentCaptor.forClass(Promotion.class);
        verify(promotionRepository).save(promotionArgumentCaptor.capture());

        PromotionResponse mappedPromotionResponse =
                promotionMapper.mapToDto(promotionArgumentCaptor.getValue());

        assertEquals(mappedPromotionResponse, savedPromotionRequest);
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
        PromotionRequesst promotionRequesst = createPromotionRequest(BigDecimal.valueOf(999.99));

        when(productService.findProductByIdWithPromotion(anyLong())).thenReturn(product);

        assertThrows(PromotionPriceGreaterThanBasePriceException.class, () -> underTest.savePromotion(promotionRequesst));
        verify(productService, times(1)).findProductByIdWithPromotion(1L);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(promotionRepository);
    }

    @Test
    void willThrowWhenProductHasAlreadyPromotion() {
        Promotion promotion = make(a(BASIC_PROMOTION));
        Product product = make(a(BASIC_PRODUCT).but(with(PRICE_PROMOTIONS, Set.of(promotion))));

        PromotionRequesst promotionRequesst = PromotionBuilderHelper.createPromotionRequest();

        when(productService.findProductByIdWithPromotion(anyLong())).thenReturn(product);

        assertThrows(ProductHasAlreadyPromotionException.class, () -> underTest.savePromotion(promotionRequesst));
        verify(productService, times(1)).findProductByIdWithPromotion(1L);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(promotionRepository);
    }
}
