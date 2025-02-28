package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSubscriberService {

    private final ProductSubscriberRepository subscriberRepository;
    private final Clock clock;

    private static final String RESPONSE_MESSAGE = "Subscriber is unsubscribed.";

    public ProductSubscriber findWithSubscriptionById(Long id) {
        return subscriberRepository.findWithSubscriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductSubscriber", "id", id));
    }

    public ProductSubscriber saveOrReturnExistEntity(SubscriberRequest subscriber) {
        log.info("Saving subscriber with email: {}", subscriber.email());
        return subscriberRepository.findByEmail(subscriber.email()).orElseGet(() -> {
                    log.debug("Subscriber with email: {}:  not exist, saved it.", subscriber.email());
                    return subscriberRepository.save(
                            ProductSubscriber.builder()
                                    .email(subscriber.email())
                                    .enabled(Boolean.FALSE)
                                    .subscriptions(new HashSet<>())
                                    .build()
                    );
                }
        );
    }

    public ResponseMessageDTO delete(ProductSubscriber subscriber) {
        log.info("Deleting subscriber with email: {}", subscriber.getEmail());
        subscriberRepository.delete(subscriber);
        return new ResponseMessageDTO(RESPONSE_MESSAGE);
    }

    public Boolean isFirstRegistration(ProductSubscriber subscriber) {
        LocalDateTime now = LocalDateTime.now(clock);
        return Boolean.FALSE.equals(subscriber.getEnabled()) &&
                subscriber.getCreatedAt().isBefore(now.plusMinutes(1)) &&
                subscriber.getCreatedAt().isAfter(now.minusMinutes(1));
    }
}
