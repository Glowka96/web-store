package com.example.portfolio.webstorespring.services.emails.strategy.impl.newsletters;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ConfirmNewsletterEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getNotificationType() {
        return EmailType.CONFIRM_NEWSLETTER;
    }

    @Override
    public String getEmailTitle() {
        return "Confirm newsletter subscription";
    }

    @Override
    public String getEmailMessage() {
        return "To confirm newsletter subscription, please click here: \n"
                + linkProvider.getNewsletter() + "%s";
    }
}
