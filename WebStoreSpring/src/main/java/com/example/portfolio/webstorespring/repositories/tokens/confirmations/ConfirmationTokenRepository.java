package com.example.portfolio.webstorespring.repositories.tokens.confirmations;

import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.BaseConfToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ConfirmationTokenRepository<T extends BaseConfToken> extends JpaRepository<T, Long> {

    Optional<T> findByTokenDetails_Token(String token);
}
