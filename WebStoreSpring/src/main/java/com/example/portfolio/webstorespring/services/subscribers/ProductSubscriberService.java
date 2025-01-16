package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.model.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSubscriberService  {

   private final ProductSubscriberRepository productSubscriberRepository;
   private final Clock clock;

    public ProductSubscriber saveOrReturnExistEntity(SubscriberRequest subscriber) {
        log.info("Saving subscriber with email: {}", subscriber.email());
        Optional<ProductSubscriber> productSubscriber = productSubscriberRepository.findByEmail(subscriber.email());
        if(productSubscriber.isPresent()) {
            log.debug("Product subscriber with email: {} already exists. Returning it.", subscriber.email());
            return productSubscriber.get();
        }
        log.info("Saved subscriber with email: {}", subscriber.email());
        return productSubscriberRepository.save(
                ProductSubscriber.builder()
                        .email(subscriber.email())
                        .enabled(Boolean.FALSE)
                        .build()
        );
    }

    public Map<String, Object> remove(ProductSubscriber subscriber) {
        log.info("Deleting subscriber with email: {}", subscriber.getEmail());
        productSubscriberRepository.delete(subscriber);
        return Map.of("message", "Subscriber is unsubscribed.");
    }

    public Boolean isFirstRegistration(ProductSubscriber productSubscriber) {
        LocalDateTime now = LocalDateTime.now(clock);
        return Boolean.FALSE.equals(productSubscriber.getEnabled()) &&
               productSubscriber.getCreatedAt().isBefore(now.plusMinutes(1)) &&
               productSubscriber.getCreatedAt().isAfter(now.minusMinutes(1));
    }

}
