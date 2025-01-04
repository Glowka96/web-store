package com.example.portfolio.webstorespring.services.confirmations;

import com.example.portfolio.webstorespring.model.entity.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.model.entity.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.repositories.confirmations.ConfirmationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductConfTokenService extends AbstractConfTokenService<ProductConfToken, ProductSubscriber> {
    public ProductConfTokenService(ConfirmationTokenRepository<ProductConfToken> tokenRepository, TokenDetailsService tokenDetailsService) {
        super(tokenRepository, tokenDetailsService);
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
