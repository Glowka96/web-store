package com.example.portfolio.webstorespring.services.tokens.removals;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.entity.subscribers.Subscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.RemovalToken;
import com.example.portfolio.webstorespring.repositories.tokens.removals.RemovalTokenRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractRemovalTokenService<T extends RemovalToken, S extends Subscriber> {

    private final RemovalTokenRepository<T> removalTokenRepository;

    public T getByToken(String token) {
        return removalTokenRepository.findByToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("Removal token","token", token));
    }

    public T save(S subscriber) {
        return removalTokenRepository.save(createRemovalEntity(subscriber));
    }

    public void delete(String token) {
        removalTokenRepository.deleteByToken(token);
    }

    protected abstract T createRemovalEntity(S subscriber);
}
