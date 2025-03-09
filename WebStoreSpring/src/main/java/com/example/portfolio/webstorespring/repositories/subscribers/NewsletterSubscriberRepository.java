package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {

    Set<NewsletterSubscriber> findAllByEnabledIsTrue();

    Boolean existsByEmail(String email);
}
