package com.example.portfolio.webstorespring.repositories.tokens.removals;

import com.example.portfolio.webstorespring.models.entity.tokens.removals.SingleProductRemovalToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SingleProductRemovalRepository extends JpaRepository<SingleProductRemovalToken, Long> {

    @Query("""
            SELECT token FROM SingleProductRemovalToken token
            LEFT JOIN FETCH token.subscriber
            WHERE token.token = :token
            """)
    Optional<SingleProductRemovalToken> findByToken(@Param("token") String token);
    void deleteByToken(String token);
}
