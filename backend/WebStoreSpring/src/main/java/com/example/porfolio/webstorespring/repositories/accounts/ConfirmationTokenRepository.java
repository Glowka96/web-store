package com.example.porfolio.webstorespring.repositories.accounts;

import com.example.porfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);
}