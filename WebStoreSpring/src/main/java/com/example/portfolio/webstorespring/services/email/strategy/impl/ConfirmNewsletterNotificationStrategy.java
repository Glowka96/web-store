package com.example.portfolio.webstorespring.services.email.strategy.impl;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmNewsletterNotificationStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.CONFIRM_NEWSLETTER;
    }

    @Override
    public String getEmailTitle() {
        return "Confirm newsletter subscription";
    }

    @Override
    public String getEmailMessage() {
        return "To confirm newsletter subscription, please click here: \n"
                + linkProvider.getNewsletter();
    }
}
