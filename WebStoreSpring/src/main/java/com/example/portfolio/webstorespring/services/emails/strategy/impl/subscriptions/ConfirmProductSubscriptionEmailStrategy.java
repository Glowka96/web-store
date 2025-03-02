package com.example.portfolio.webstorespring.services.emails.strategy.impl.subscriptions;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmProductSubscriptionEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getEmailType() {
        return EmailType.CONFIRM_PRODUCT_SUBSCRIPTION;
    }

    @Override
    public String getEmailTitle() {
        return "Confirm product subscription";
    }

    @Override
    public String getEmailMessage() {
        return "To confirm your product subscription, please click here: \n"
                + linkProvider.getProductSubscription() + "%s";
    }
}
