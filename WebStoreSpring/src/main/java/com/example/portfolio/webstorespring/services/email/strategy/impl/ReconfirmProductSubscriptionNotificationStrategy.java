package com.example.portfolio.webstorespring.services.email.strategy.impl;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReconfirmProductSubscriptionNotificationStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.RECONFIRM_PRODUCT_SUBSCRIPTION;
    }

    @Override
    public String getEmailTitle() {
        return "Reconfirm product subscription";
    }

    @Override
    public String getEmailMessage() {
        return "To reconfirm your product subscription, please click here: \n"
               + linkProvider.getProductSubscription();
    }
}
