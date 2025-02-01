package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductConfTokenService extends AbstractConfTokenService<ProductConfToken, ProductSubscriber> {


    public ProductConfTokenService(ConfirmationTokenRepository<ProductConfToken> tokenRepository,
                                   TokenDetailsService tokenDetailsService,
                                   NotificationExpirationManager notificationExpirationManager) {
        super(tokenRepository, tokenDetailsService, notificationExpirationManager);
    }

    @Override
    protected ProductConfToken createTokenEntity(ProductSubscriber relatedEntity, TokenDetails tokenDetails) {
        return ProductConfToken.builder()
                .subscriber(relatedEntity)
                .tokenDetails(tokenDetails)
                .build();
    }

    @Override
    public ProductSubscriber extractRelatedEntity(ProductConfToken tokenEntity) {
        return tokenEntity.getSubscriber();
    }
}
