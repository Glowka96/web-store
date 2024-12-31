package com.example.portfolio.webstorespring.services.confirmations;

import com.example.portfolio.webstorespring.model.entity.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.model.entity.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.model.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.repositories.confirmations.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class NewsletterTokenService extends AbstractConfTokenService<NewsletterConfToken, NewsletterSubscriber> {
    public NewsletterTokenService(ConfirmationTokenRepository<NewsletterConfToken> tokenRepository, TokenDetailsService tokenDetailsService) {
        super(tokenRepository, tokenDetailsService);
    }

    @Override
    protected NewsletterConfToken createTokenEntity(NewsletterSubscriber relatedEntity, TokenDetails tokenDetails) {
        return NewsletterConfToken.builder()
                .subscriber(relatedEntity)
                .tokenDetails(tokenDetails)
                .build();
    }

    @Override
    protected NewsletterSubscriber extractRelatedEntity(NewsletterConfToken tokenEntity) {
        return tokenEntity.getSubscriber();
    }
}
