package com.example.portfolio.webstorespring.buildhelpers.subscribers;

import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterSubscriber;

public class NewsletterSubscriberBuilderHelper {

    public static NewsletterSubscriber createEnabledNewsletterSubscriber() {
        return createNewsletterSubscriber(Boolean.TRUE);
    }

    public static NewsletterSubscriber createDisabledNewsletterSubscriber() {
        return createNewsletterSubscriber(Boolean.FALSE);
    }

    private static NewsletterSubscriber createNewsletterSubscriber(Boolean enabled) {
        return NewsletterSubscriber.builder()
                .id(1L)
                .email("test@test.pl")
                .enabled(enabled)
                .build();
    }
}
