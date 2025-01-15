package com.example.portfolio.webstorespring.services.subscribers;

import com.example.portfolio.webstorespring.model.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.model.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.repositories.subscribers.NewsletterSubscriberRepository;
import com.example.portfolio.webstorespring.repositories.subscribers.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NewsletterSubscriberService extends AbstractSubscriberService<NewsletterSubscriber> {

    private final NewsletterSubscriberRepository newsletterSubscriberRepository;

    public NewsletterSubscriberService(SubscriberRepository<NewsletterSubscriber> subscriberRepository, NewsletterSubscriberRepository newsletterSubscriberRepository) {
        super(subscriberRepository);
        this.newsletterSubscriberRepository = newsletterSubscriberRepository;
    }


    public Set<NewsletterSubscriber> getAllEnabled() {
        return newsletterSubscriberRepository.findAllByEnabledIsTrue();
    }

    @Override
    protected NewsletterSubscriber createEntity(SubscriberRequest subscriber) {
        return NewsletterSubscriber.builder()
                .email(subscriber.email())
                .enabled(Boolean.FALSE)
                .build();
    }
}

