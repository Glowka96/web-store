package com.example.portfolio.webstorespring.services.email.strategy.impl;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WelcomeProductSubscriptionNotificationStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.WELCOME_PRODUCT_SUBSCRIPTION;
    }

    @Override
    public String getEmailTitle() {
        return "Welcome to newsletter";
    }

    @Override
    public String getEmailMessage() {
        return "Hi, Welcome to the ToysLand family! \n" +
               "We're thrilled to have you on board. By joining our product subscription, you'll be the first to know about:\n\n" +
               "When the product you subscribe to is available" +
               "Thank you for trusting us to keep you informed about your favorite toys.\n\n" +
               "If you want to unsubscribe newsletter clink here: " +
               linkProvider.getUnsubscribeProductSubscription();
    }
}
