package com.example.portfolio.webstorespring.services.email.strategy.impl.newsletters;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.email.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReconfirmNewsletterNotificationStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.RECONFIRM_NEWSLETTER;
    }

    @Override
    public String getEmailTitle() {
        return "Re-confirm newsletter subscription";
    }

    @Override
    public String getEmailMessage() {
        return "To re-confirm newsletter subscription, please click here: \n"
                + linkProvider.getNewsletter();
    }
}
