package com.example.portfolio.webstorespring.repositories.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ConfToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ConfirmationTokenRepository<T extends ConfToken> extends JpaRepository<T, Long> {

    Optional<T> findByTokenDetails_Token(String token);
}
