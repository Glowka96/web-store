package com.example.portfolio.webstorespring.services.emails.registrations;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.ProductSubscriberRequest;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriberService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.ProductConfTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.ProductRemovalTokenService;
import com.example.portfolio.webstorespring.services.tokens.removals.SingleProductRemovalTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterProductSubscriberService extends AbstractConfirmEmailService<ProductConfToken, ProductSubscriber, ProductConfTokenService> {

    private final ProductSubscriptionService productSubscriptionService;
    private final ProductSubscriberService productSubscriberService;
    private final ProductRemovalTokenService productRemovalTokenService;
    private final SingleProductRemovalTokenService singleProductRemovalTokenService;

    private static final String RESPONSE_MESSAGE = "You have successfully subscribed to this product.";

    RegisterProductSubscriberService(EmailSenderService emailSenderService,
                                     ProductConfTokenService confirmationTokenService,
                                     ProductSubscriptionService productSubscriptionService,
                                     ProductSubscriberService productSubscriberService,
                                     ProductRemovalTokenService productRemovalTokenService, SingleProductRemovalTokenService singleProductRemovalTokenService) {
        super(emailSenderService, confirmationTokenService);
        this.productSubscriptionService = productSubscriptionService;
        this.productSubscriberService = productSubscriberService;
        this.productRemovalTokenService = productRemovalTokenService;
        this.singleProductRemovalTokenService = singleProductRemovalTokenService;
    }


    @Transactional
    public ResponseMessageDTO register(ProductSubscriberRequest productSubscriberRequest) {
        ProductSubscriber productSubscriber = productSubscriberService.saveOrReturnExistEntity(productSubscriberRequest.subscriberRequest());
        productSubscriptionService.add(productSubscriber, productSubscriberRequest.productId());

        if (Boolean.TRUE.equals(productSubscriberService.isFirstRegistration(productSubscriber))) {
            sendConfirmationEmail(productSubscriber, EmailType.CONFIRM_PRODUCT_SUBSCRIPTION);
        }

        SingleProductRemovalToken singleProductRemovalToken = singleProductRemovalTokenService.save(productSubscriber, productSubscriberRequest.productId());
        ProductRemovalToken productRemovalToken = productRemovalTokenService.save(productSubscriber);
        sendEmail(EmailType.WELCOME_PRODUCT_SUBSCRIPTION,
                productSubscriber.getEmail(),
                singleProductRemovalToken.getToken(),
                productRemovalToken.getToken()
        );
        return new ResponseMessageDTO(RESPONSE_MESSAGE);
    }

    @Transactional
    public ResponseMessageDTO confirm(String token) {
        return confirmTokenOrResend(token, EmailType.RECONFIRM_PRODUCT_SUBSCRIPTION);
    }

    @Override
    protected void executeAfterConfirm(ProductSubscriber ownerToken) {
        ownerToken.setEnabled(Boolean.TRUE);
    }
}
