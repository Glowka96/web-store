package com.example.portfolio.webstorespring.services.emails.notifications;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.subscribers.ProductSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyProductSubscribersService {

    private final EmailSenderService emailSenderService;
    private final ProductSubscriptionService productSubscriptionService;

    @Async
    public void notifyEnabledSubscribers(Long productId, String productName) {
        log.info("Starting send notification for subscribers.");
        ProductSubscription productSubscription = productSubscriptionService.getWithEnabledSubscribersByProductId(productId);
        productSubscription.getProductSubscribers().forEach(
                subscriber -> emailSenderService.sendEmail(EmailType.AVAILABLE_PRODUCT, subscriber.getEmail(), productName)
        );
        log.info("Sent all emails.");
    }
}
