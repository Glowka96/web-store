package com.example.portfolio.webstorespring.repositories.accounts;

import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountConfTokenRepository extends JpaRepository<AccountConfToken, Long> {

    Optional<AccountConfToken> findByTokenDetails_Token(String token);
}
