package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.RemovalTokenRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NewsletterRemovalTokenService extends AbstractRemovalTokenService<NewsletterRemovalToken, NewsletterSubscriber> {

    public NewsletterRemovalTokenService(RemovalTokenRepository<NewsletterRemovalToken> removalTokenRepository) {
        super(removalTokenRepository);
    }

    @Override
    protected NewsletterRemovalToken createRemovalEntity(NewsletterSubscriber subscriber) {
        return NewsletterRemovalToken.builder()
                .token(UUID.randomUUID().toString())
                .subscriber(subscriber)
                .build();
    }
}
