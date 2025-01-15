package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.model.entity.subscribers.NewsletterSubscriber;

import java.util.Set;

public interface NewsletterSubscriberRepository extends SubscriberRepository<NewsletterSubscriber> {
    Set<NewsletterSubscriber> findAllByEnabledIsTrue();
}
