package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.exceptions.SubscriberAlreadyRegisterException;
import com.example.portfolio.webstorespring.model.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.model.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.repositories.subscribers.NewsletterSubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsletterSubscriberService {

    private final NewsletterSubscriberRepository newsletterSubscriberRepository;

    public Set<NewsletterSubscriber> getAllEnabled() {
        return newsletterSubscriberRepository.findAllByEnabledIsTrue();
    }

    public NewsletterSubscriber save(SubscriberRequest subscriber) {
        log.info("Saving subscriber with email: {}", subscriber.email());
        if (Boolean.TRUE.equals(newsletterSubscriberRepository.existsByEmail(subscriber.email()))) {
            throw new SubscriberAlreadyRegisterException(subscriber.email());
        }
        log.info("Saved subscriber with email: {}", subscriber.email());
        return newsletterSubscriberRepository.save(
                NewsletterSubscriber.builder()
                        .email(subscriber.email())
                        .enabled(Boolean.FALSE)
                        .build()
        );
    }

    public Map<String, Object> remove(NewsletterSubscriber subscriber) {
        log.info("Deleting subscriber with email: {}", subscriber.getEmail());
        newsletterSubscriberRepository.delete(subscriber);
        return Map.of("message", "Subscriber is unsubscribed.");
    }
}

