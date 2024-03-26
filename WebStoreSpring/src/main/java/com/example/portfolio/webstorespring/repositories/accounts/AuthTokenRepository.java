package com.example.portfolio.webstorespring.repositories.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    @Query(value = """
            SELECT at FROM AuthToken at INNER JOIN Account a
            ON at.account.id = a.id
            WHERE a.id = :id AND (at.expired = false OR at.revoked = false)
            """)
    List<AuthToken> findAllValidTokenByAccountId(@Param("id") Long id);

    Optional<AuthToken> findByToken(String token);
}
