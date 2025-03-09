package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.RemovalTokenRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductRemovalTokenService extends AbstractRemovalTokenService<ProductRemovalToken, ProductSubscriber> {

    public ProductRemovalTokenService(RemovalTokenRepository<ProductRemovalToken> removalTokenRepository) {
        super(removalTokenRepository);
    }

    @Override
    protected ProductRemovalToken createRemovalEntity(ProductSubscriber subscriber) {
        return ProductRemovalToken.builder()
                .token(UUID.randomUUID().toString())
                .subscriber(subscriber)
                .build();
    }
}
