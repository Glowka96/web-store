package com.example.portfolio.webstorespring.services.emails.strategy.impl.newsletters;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.services.emails.strategy.EmailStrategy;

public class NewsletterMessageEmailStrategy implements EmailStrategy {

    @Override
    public EmailType getNotificationType() {
        return EmailType.NEWSLETTER_MESSAGE;
    }

    @Override
    public String getEmailTitle() {
        return "Your Latest Webshop Newsletter is Here!";
    }

    @Override
    public String getEmailMessage() {
        return """
            Hello,
            We are excited to bring you the latest updates and offers from our webshop. Here's the message from our newsletter team:

            %s

            Best regards,
            The Webshop Team
            """;
    }
}
