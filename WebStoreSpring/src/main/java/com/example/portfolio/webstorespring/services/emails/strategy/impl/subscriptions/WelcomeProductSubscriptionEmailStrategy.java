package com.example.portfolio.webstorespring.services.emails.strategy.impl.subscriptions;

import com.example.portfolio.webstorespring.configs.providers.ConfirmationLinkProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WelcomeProductSubscriptionEmailStrategy implements EmailStrategy {

    private final ConfirmationLinkProvider linkProvider;

    @Override
    public EmailType getNotificationType() {
        return EmailType.WELCOME_PRODUCT_SUBSCRIPTION;
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
                "If you want to unsubscribe this product click here: " +
                linkProvider.getUnsubscribeSingleProduct() + "%s \n\n" +
                "If you want to unsubscribe all products clink here: " +
                linkProvider.getUnsubscribeProductSubscription() + "%s";
    }
}
