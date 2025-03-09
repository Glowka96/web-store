package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.SingleProductRemovalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SingleProductRemovalTokenService {

    private final SingleProductRemovalRepository removalTokenRepository;

    public SingleProductRemovalToken getByToken(String token) {
        log.info("Fetching removal token for token {}", token);
        return removalTokenRepository.findByToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("Removal token","token", token));
    }

    public SingleProductRemovalToken save(ProductSubscriber subscriber, Long productId) {
        log.info("Saving removal token for subscriber email: {} and product id {}", subscriber.getEmail(), productId);
        return removalTokenRepository.save(SingleProductRemovalToken.builder()
                .token(UUID.randomUUID().toString())
                .subscriber(subscriber)
                .productId(productId)
                .build());
    }

    public void deleteByToken(String token) {
        log.info("Deleting removal token for token {}", token);
        removalTokenRepository.deleteByToken(token);
    }
}
