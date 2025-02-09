package com.example.portfolio.webstorespring.services.unsubscribes;

import com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriptionBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.tokens.removals.SingleProductRemovalTokenBuilderHelper;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import com.example.portfolio.webstorespring.services.tokens.removals.ProductRemovalTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.SingleProductRemovalTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.portfolio.webstorespring.buildhelpers.tokens.removals.ProductRemovalTokenBuilderHelper.createProductRemovalTokenWithEnabledSubscriber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UnsubscribeProductServiceTest {

    @Mock
    private ProductRemovalTokenService productRemovalTokenService;
    @Mock
    private SingleProductRemovalTokenService singleProductRemovalTokenService;
    @Mock
    private ProductSubscriberService productSubscriberService;
    @Mock
    private ProductSubscriptionService productSubscriptionService;
    @InjectMocks
    private UnsubscribeProductService underTest;

    @Test
    void shouldDeleteAllSubscriptions() {
        ProductRemovalToken removalToken = createProductRemovalTokenWithEnabledSubscriber();
        ProductSubscriber productSubscriber = removalToken.getSubscriber();

        ProductSubscription productSubscription = ProductSubscriptionBuilderHelper.createProductSubscription();
        ProductSubscription productSubscription2 = ProductSubscriptionBuilderHelper.createProductSubscription();
        productSubscription.addSubscriber(productSubscriber);
        productSubscription2.addSubscriber(productSubscriber);

        ResponseMessageDTO response = new ResponseMessageDTO("Your all subscription was removed");

        given(productRemovalTokenService.getByToken(anyString())).willReturn(removalToken);
        given(productSubscriberService.findWithSubscriptionById(anyLong())).willReturn(productSubscriber);

        ResponseMessageDTO result = underTest.deleteAllSubscriptions("token123");
        verify(productSubscriberService, times(1)).delete(productSubscriber);
        verify(productRemovalTokenService, times(1)).deleteByToken(removalToken.getToken());

        assertEquals(response, result);
        assertTrue(productSubscriber.getSubscriptions().isEmpty());
    }

    @Test
    void shouldDeleteFromSingleProduct() {
        SingleProductRemovalToken removalToken = SingleProductRemovalTokenBuilderHelper.createNewsletterRemovalTokenWithEnabledSubscriber();
        ResponseMessageDTO response = new ResponseMessageDTO("Your subscription for this: test was removed.");

        given(singleProductRemovalTokenService.getByToken(anyString())).willReturn(removalToken);
        given(productSubscriptionService.removeForSingleProduct(any(ProductSubscriber.class), anyLong())).willReturn(response);

        ResponseMessageDTO result = underTest.deleteFromSingleProduct("token123");
        verify(productRemovalTokenService, times(1)).deleteByToken(removalToken.getToken());

        assertEquals(response, result);
    }
}