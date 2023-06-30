package com.example.porfolio.webstorespring.repositories.accounts;

import com.example.porfolio.webstorespring.model.entity.accounts.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    @Query(value = """
      select at from AuthToken at inner join Account a\s
      on at.account.id = a.id\s
      where a.id = :id and (at.expired = false or at.revoked = false)\s
      """)
    List<AuthToken> findAllValidTokenByAccountId(@Param("id") Long id);

    Optional<AuthToken> findByToken(String token);
}
