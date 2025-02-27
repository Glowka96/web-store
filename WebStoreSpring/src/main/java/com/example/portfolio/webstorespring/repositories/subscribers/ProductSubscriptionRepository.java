package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductSubscriptionRepository extends JpaRepository<ProductSubscription, Long> {

    @Query("""
                SELECT ps FROM ProductSubscription ps
                JOIN FETCH ps.productSubscribers s
                WHERE ps.id = :productId AND s.enabled = true
            """)
    Optional<ProductSubscription> findByIdWithEnabledSubscribers(@Param("productId") Long productId);

    @Query("""
                SELECT ps FROM ProductSubscription ps
                LEFT JOIN FETCH ps.productSubscribers s
                WHERE ps.id = :productId AND s.email = :email
            """)
    Optional<ProductSubscription> findByIdAndSubscriberEmail(@Param("productId") Long productId, @Param("email") String email);
}
