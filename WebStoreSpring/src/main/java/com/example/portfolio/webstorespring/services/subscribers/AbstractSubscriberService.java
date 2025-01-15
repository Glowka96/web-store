package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.exceptions.SubscriberAlreadyRegisterException;
import com.example.portfolio.webstorespring.model.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.model.entity.subscribers.Subscriber;
import com.example.portfolio.webstorespring.repositories.subscribers.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractSubscriberService<T extends Subscriber> {

    private final SubscriberRepository<T> subscriberRepository;

    public T save(SubscriberRequest subscriber) {
        log.info("Saving subscriber with email: {}", subscriber.email());
        if (subscriberRepository.existsByEmail(subscriber.email())) {
            throw new SubscriberAlreadyRegisterException(subscriber.email());
        }
        log.info("Saved subscriber with email: {}", subscriber.email());
        return subscriberRepository.save(createEntity(subscriber));
    }

    public Map<String, Object> remove(T subscriber) {
        log.info("Deleting subscriber with email: {}", subscriber.getEmail());
        subscriberRepository.delete(subscriber);
        return Map.of("message", "Subscriber is unsubscribed.");
    }

    protected abstract T createEntity(SubscriberRequest subscriber);
}
