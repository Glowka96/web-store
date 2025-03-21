package com.example.portfolio.webstorespring.services.unsubscribes;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.models.entities.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import com.example.portfolio.webstorespring.services.tokens.removals.ProductRemovalTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.SingleProductRemovalTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnsubscribeProductService {

    private final ProductRemovalTokenService productRemovalTokenService;
    private final SingleProductRemovalTokenService singleProductRemovalTokenService;
    private final ProductSubscriberService subscriberService;
    private final ProductSubscriptionService subscriptionService;

    private static final String RESPONSE_MESSAGE = "Your all subscription was removed";

    @Transactional
    public ResponseMessageDTO deleteAllSubscriptions(String token) {
        ProductRemovalToken productRemovalToken = productRemovalTokenService.getByToken(token);
        ProductSubscriber subscriber = subscriberService.findWithSubscriptionById(productRemovalToken.getId());
        subscriber.getSubscriptions().forEach(
                subscription -> subscription.removeSubscriber(subscriber)
        );
        subscriberService.delete(subscriber);
        productRemovalTokenService.deleteByToken(token);
        return new ResponseMessageDTO(RESPONSE_MESSAGE);
    }

    @Transactional
    public ResponseMessageDTO deleteFromSingleProduct(String token) {
        SingleProductRemovalToken singleProductRemovalToken = singleProductRemovalTokenService.getByToken(token);
        ResponseMessageDTO responseMessageDTO = subscriptionService.removeForSingleProduct(singleProductRemovalToken.getSubscriber(), singleProductRemovalToken.getProductId());
        singleProductRemovalTokenService.deleteByToken(token);
        return responseMessageDTO;
    }
}
