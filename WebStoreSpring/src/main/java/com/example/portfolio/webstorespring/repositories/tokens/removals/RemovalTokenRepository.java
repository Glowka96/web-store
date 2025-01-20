package com.example.portfolio.webstorespring.repositories.tokens.removals;

import com.example.portfolio.webstorespring.model.entity.tokens.removals.RemovalToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface RemovalTokenRepository<T extends RemovalToken> extends JpaRepository<T, Long> {
    Optional<T> findByToken(String token);

    void deleteByToken(String token);
}
