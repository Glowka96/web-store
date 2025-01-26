package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.SingleProductRemovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SingleProductRemovalTokenService {

    private final SingleProductRemovalRepository removalTokenRepository;

    public SingleProductRemovalToken create(ProductSubscriber subscriber, Long productId) {
        return removalTokenRepository.save(SingleProductRemovalToken.builder()
                .subscriber(subscriber)
                .productId(productId)
                .build());
    }
}
