package com.example.portfolio.webstorespring.services.unsubscribes;

import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import com.example.portfolio.webstorespring.services.tokens.removals.ProductRemovalTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.SingleProductRemovalTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnsubscribeProduct {

    private final ProductRemovalTokenService productRemovalTokenService;
    private final SingleProductRemovalTokenService singleProductRemovalTokenService;
    private final ProductSubscriberService subscriberService;
    private final ProductSubscriptionService subscriptionService;

    @Transactional
    public Map<String, Object> deleteAllSubscriptions(String token) {
        ProductRemovalToken productRemovalToken = productRemovalTokenService.getByToken(token);
        ProductSubscriber subscriber = subscriberService.findWithSubscriptionById(productRemovalToken.getId());
        subscriber.getSubscription().forEach(
                subscription -> subscription.removeSubscriber(subscriber)
        );
        subscriberService.delete(subscriber);
        productRemovalTokenService.delete(token);
        return Map.of("message", "Your all subscription was removed");
    }

    @Transactional
    public Map<String, Object> deleteFromSingleProduct(String token) {
        SingleProductRemovalToken singleProductRemovalToken = singleProductRemovalTokenService.getByToken(token);
        Map<String, Object> result = subscriptionService.removeForSingleProduct(singleProductRemovalToken.getSubscriber(), singleProductRemovalToken.getProductId());
        productRemovalTokenService.delete(token);
        return result;
    }
}
