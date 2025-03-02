package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterMessageRepository extends JpaRepository<NewsletterMessage, Long> {
}
