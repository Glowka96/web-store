package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.SingleProductRemovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SingleProductRemovalTokenService {

    private final SingleProductRemovalRepository removalTokenRepository;

    public SingleProductRemovalToken getByToken(String token) {
        return removalTokenRepository.findByToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("Removal token","token", token));
    }

    public SingleProductRemovalToken save(ProductSubscriber subscriber, Long productId) {
        return removalTokenRepository.save(SingleProductRemovalToken.builder()
                .subscriber(subscriber)
                .productId(productId)
                .build());
    }

    public void deleteByToken(String token) {
        removalTokenRepository.deleteByToken(token);
    }
}
