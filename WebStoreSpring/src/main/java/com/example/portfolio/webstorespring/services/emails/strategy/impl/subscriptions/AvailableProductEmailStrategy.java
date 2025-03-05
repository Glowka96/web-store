package com.example.portfolio.webstorespring.services.emails.strategy.impl.subscriptions;

import com.example.portfolio.webstorespring.configs.providers.CorsProvider;
import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvailableProductEmailStrategy implements EmailStrategy {

    private final CorsProvider corsProvider;

    @Override
    public EmailType getEmailType() {
        return EmailType.AVAILABLE_PRODUCT;
    }

    @Override
    public String getEmailTitle() {
        return "Your product is Now Back in Stock!";
    }

    @Override
    public String getEmailMessage() {
        return """
                Hello,
                We are pleased to inform you that the product: %s is now available on our webshop.
                You can visit our store to make a purchase at your convenience.
                Visit the shop:
                """ +
                corsProvider.getAllowedOriginPatterns() + "/api/v1/products/%s" +
                """
                
                
                Best regards,
                The Webshop Team
                """;
    }
}
