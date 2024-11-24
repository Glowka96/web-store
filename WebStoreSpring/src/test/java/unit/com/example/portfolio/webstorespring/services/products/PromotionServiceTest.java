package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.buildhelpers.products.PromotionBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ProductHasAlreadyPromotionException;
import com.example.portfolio.webstorespring.exceptions.PromotionPriceGreaterThanBasePriceException;
import com.example.portfolio.webstorespring.mappers.PromotionMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.PromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import com.example.portfolio.webstorespring.model.entity.products.Promotion;
import com.example.portfolio.webstorespring.repositories.products.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @InjectMocks
    private PromotionService underTest;

    @Test
    void shouldSavePromotion() {
        Product product = make(a(BASIC_PRODUCT).but(with(PRICE_PROMOTIONS, Set.of())));
        PromotionRequest promotionRequest = PromotionBuilderHelper.createPromotionRequest();

        given(productService.findWithPromotionById(anyLong())).willReturn(product);

        PromotionResponse savedPromotionRequest =
                underTest.save(promotionRequest);

        ArgumentCaptor<Promotion> promotionArgumentCaptor =
                ArgumentCaptor.forClass(Promotion.class);
        verify(promotionRepository).save(promotionArgumentCaptor.capture());

        PromotionResponse mappedPromotionResponse =
                PromotionMapper.mapToDto(promotionArgumentCaptor.getValue());

        assertEquals(mappedPromotionResponse, savedPromotionRequest);
    }

    @Test
    void willThrowPromotionPriceGreaterThanBasePriceException() {
        Product product = make(a(BASIC_PRODUCT).but(with(PRICE_PROMOTIONS, Set.of())));
        PromotionRequest promotionRequest = createPromotionRequest(BigDecimal.valueOf(999.99));

        when(productService.findWithPromotionById(anyLong())).thenReturn(product);

        assertThrows(PromotionPriceGreaterThanBasePriceException.class, () -> underTest.save(promotionRequest));
        verify(productService, times(1)).findWithPromotionById(1L);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(promotionRepository);
    }

    @Test
    void willThrowWhenProductHasAlreadyPromotion() {
        Promotion promotion = make(a(BASIC_PROMOTION));
        Product product = make(a(BASIC_PRODUCT).but(with(PRICE_PROMOTIONS, Set.of(promotion))));

        PromotionRequest promotionRequest = PromotionBuilderHelper.createPromotionRequest();

        when(productService.findWithPromotionById(anyLong())).thenReturn(product);

        assertThrows(ProductHasAlreadyPromotionException.class, () -> underTest.save(promotionRequest));
        verify(productService, times(1)).findWithPromotionById(1L);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(promotionRepository);
    }
}
