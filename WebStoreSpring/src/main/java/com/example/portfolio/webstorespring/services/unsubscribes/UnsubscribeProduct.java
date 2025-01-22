package com.example.portfolio.webstorespring.services.unsubscribes;

import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.tokens.removals.ProductRemovalTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnsubscribeProduct {

    private final ProductRemovalTokenService removalTokenService;
    private final ProductSubscriberService subscriberService;

    @Transactional
    public void deleteSubscriberAndHisSubscriptions(String token) {
        ProductRemovalToken productRemovalToken = removalTokenService.get(token);
        ProductSubscriber subscriber = subscriberService.findWithSubscriptionById(productRemovalToken.getId());
        subscriber.getSubscription().forEach(
                subscription -> subscription.removeSubscriber(subscriber)
        );
        subscriberService.delete(subscriber);
        removalTokenService.delete(token);
    }
}
