package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.models.entities.subscribers.NewsletterMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsletterMessageRepository extends JpaRepository<NewsletterMessage, Long> {

    @Query("""
            SELECT n FROM NewsletterMessage n
            WHERE n.sendDate > CURRENT_TIMESTAMP
            """)
    List<NewsletterMessage> findFutureMessages();
}
