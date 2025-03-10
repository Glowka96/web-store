package com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.TokenDetails;

public class NewsletterConfTokenBuilderHelper {

    public static NewsletterConfToken createNewsletterConfToken(NewsletterSubscriber subscriber, TokenDetails tokenDetails) {
        return NewsletterConfToken.builder()
                .subscriber(subscriber)
                .tokenDetails(tokenDetails)
                .build();
    }
}
