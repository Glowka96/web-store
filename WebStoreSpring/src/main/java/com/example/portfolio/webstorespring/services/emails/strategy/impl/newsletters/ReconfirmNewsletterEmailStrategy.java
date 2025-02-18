package com.example.portfolio.webstorespring.services.emails.strategy.impl.newsletters;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReconfirmNewsletterEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getNotificationType() {
        return EmailType.RECONFIRM_NEWSLETTER;
    }

    @Override
    public String getEmailTitle() {
        return "Re-confirm newsletter subscription";
    }

    @Override
    public String getEmailMessage() {
        return "To re-confirm newsletter subscription, please click here: \n"
                + linkProvider.getNewsletter() + "%s";
    }
}
