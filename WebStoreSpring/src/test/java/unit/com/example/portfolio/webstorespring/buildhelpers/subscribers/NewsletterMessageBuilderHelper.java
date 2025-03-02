package com.example.portfolio.webstorespring.buildhelpers.subscribers;

import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterMessage;

public class NewsletterMessageBuilderHelper {

    public static NewsletterMessage createNewsletterMessage() {
        return NewsletterMessage.builder()
                .id(1L)
                .message("Example message")
                .build();
    }
}
