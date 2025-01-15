package com.example.portfolio.webstorespring.repositories.tokens.removals;

import com.example.portfolio.webstorespring.model.entity.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.model.entity.tokens.removals.RemovalToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RemovalTokenRepository<T extends RemovalToken> extends JpaRepository<T, Long> {
    Optional<NewsletterRemovalToken> findByToken(String token);

    void deleteByToken(String token);
}
