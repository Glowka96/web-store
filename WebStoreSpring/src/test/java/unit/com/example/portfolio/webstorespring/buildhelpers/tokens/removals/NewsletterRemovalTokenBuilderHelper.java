package com.example.portfolio.webstorespring.buildhelpers.tokens.removals;

import com.example.portfolio.webstorespring.models.entities.tokens.removals.NewsletterRemovalToken;

import static com.example.portfolio.webstorespring.buildhelpers.subscribers.NewsletterSubscriberBuilderHelper.createEnabledNewsletterSubscriber;

public class NewsletterRemovalTokenBuilderHelper {

    public static NewsletterRemovalToken createNewsletterRemovalTokenWithEnabledSubscriber() {
        return NewsletterRemovalToken.builder()
                .id(1L)
                .token("token123")
                .subscriber(createEnabledNewsletterSubscriber())
                .build();
    }
}
