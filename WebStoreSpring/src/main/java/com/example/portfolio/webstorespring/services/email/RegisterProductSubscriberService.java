package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.dto.subscribers.ProductSubscriberRequest;
import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.ProductConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.TokenDetailsService;
import com.example.portfolio.webstorespring.services.tokens.removals.ProductRemovalTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Map;

@Service
public class RegisterProductSubscriberService extends AbstractConfirmEmailService<ProductConfToken, ProductSubscriber, ProductConfTokenService> {

    private final ProductSubscriptionService productSubscriptionService;
    private final ProductSubscriberService productSubscriberService;
    private final ProductRemovalTokenService productRemovalTokenService;

    RegisterProductSubscriberService(EmailSenderService emailSenderService,
                                     ProductConfTokenService confirmationTokenService,
                                     TokenDetailsService tokenDetailsService,
                                     ProductSubscriptionService productSubscriptionService,
                                     ProductSubscriberService productSubscriberService,
                                     ProductRemovalTokenService productRemovalTokenService, Clock clock) {
        super(emailSenderService, confirmationTokenService, tokenDetailsService);
        this.productSubscriptionService = productSubscriptionService;
        this.productSubscriberService = productSubscriberService;
        this.productRemovalTokenService = productRemovalTokenService;
    }

    @Transactional
    public Map<String, Object> register(ProductSubscriberRequest productSubscriberRequest) {
        ProductSubscriber productSubscriber = productSubscriberService.saveOrReturnExistEntity(productSubscriberRequest.subscriberRequest());
        productSubscriptionService.add(productSubscriber, productSubscriberRequest.productId());
        if (Boolean.TRUE.equals(productSubscriberService.isFirstRegistration(productSubscriber))) {
            sendConfirmationEmail(productSubscriber, NotificationType.CONFIRM_PRODUCT_SUBSCRIPTION);
            return Map.of(RESPONSE_MESSAGE_KEY, "Verify your email address using the link in your email.");
        }
        return Map.of(RESPONSE_MESSAGE_KEY, "You have successfully subscribed to this product.");
    }

    @Transactional
    public Map<String, Object> confirm(String token) {
        return confirmTokenOrResend(token, NotificationType.RECONFIRM_PRODUCT_SUBSCRIPTION);
    }

    @Override
    protected void executeAfterConfirm(ProductSubscriber ownerToken) {
        ownerToken.setEnabled(Boolean.TRUE);
        ProductRemovalToken productRemovalToken = productRemovalTokenService.save(ownerToken);
        sendEmail(NotificationType.WELCOME_PRODUCT_SUBSCRIPTION, ownerToken.getEmail(), productRemovalToken.getToken());
    }
}
