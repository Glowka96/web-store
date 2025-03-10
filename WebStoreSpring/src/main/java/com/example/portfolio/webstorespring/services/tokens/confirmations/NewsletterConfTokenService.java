package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class NewsletterConfTokenService extends AbstractConfTokenService<NewsletterConfToken, NewsletterSubscriber> {


    public NewsletterConfTokenService(ConfirmationTokenRepository<NewsletterConfToken> tokenRepository,
                                      TokenDetailsService tokenDetailsService,
                                      NotificationExpirationManager notificationExpirationManager) {
        super(tokenRepository, tokenDetailsService, notificationExpirationManager);
    }

    @Override
    protected NewsletterConfToken createTokenEntity(NewsletterSubscriber relatedEntity, TokenDetails tokenDetails) {
        return NewsletterConfToken.builder()
                .subscriber(relatedEntity)
                .tokenDetails(tokenDetails)
                .build();
    }

    @Override
    public NewsletterSubscriber extractRelatedEntity(NewsletterConfToken tokenEntity) {
        return tokenEntity.getSubscriber();
    }
}
