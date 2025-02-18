package com.example.portfolio.webstorespring.services.emails.strategy.impl.newsletters;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WelcomeNewsletterEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getNotificationType() {
        return EmailType.WELCOME_NEWSLETTER;
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
               linkProvider.getUnsubscribeNewsletter() + "%s";
    }
}
