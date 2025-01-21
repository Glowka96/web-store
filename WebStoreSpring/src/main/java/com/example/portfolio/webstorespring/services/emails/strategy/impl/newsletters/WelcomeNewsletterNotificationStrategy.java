package com.example.portfolio.webstorespring.services.emails.strategy.impl.newsletters;

import com.example.portfolio.webstorespring.config.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.services.emails.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WelcomeNewsletterNotificationStrategy implements NotificationStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.WELCOME_NEWSLETTER;
    }

    @Override
    public String getEmailTitle() {
        return "Welcome to newsletter";
    }

    @Override
    public String getEmailMessage() {
        return "Hi, Welcome to the ToysLand family! \n" +
               "We're thrilled to have you on board. By joining our newsletter, you'll be the first to know about:\n\n" +
               "Promotions\n" +
               "New product launches\n" +
               "Exciting updates and tips\n" +
               "Stay tuned—great things are coming your way!" +
               "If you want to unsubscribe newsletter clink here: " +
               linkProvider.getUnsubscribeNewsletter();
    }
}
