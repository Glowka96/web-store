package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.model.entity.subscribers.ProductSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductSubscriptionRepository extends JpaRepository<ProductSubscription, Long> {

    @Query("""
        SELECT ps FROM ProductSubscription ps
        LEFT JOIN ps.productSubscribers s ON s.enabled = true
""")
    Optional<ProductSubscription> findByProductSubscribersEnabled(Long productId);
}
