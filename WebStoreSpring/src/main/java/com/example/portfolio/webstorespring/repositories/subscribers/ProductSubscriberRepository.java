package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductSubscriberRepository extends JpaRepository<ProductSubscriber, Long> {

    Optional<ProductSubscriber> findByEmail(String email);

    @Query("""
            SELECT subscriber FROM ProductSubscriber subscriber
            JOIN subscriber.subscriptions subscriptions
            WHERE subscriber.id = :id
            """)
    Optional<ProductSubscriber> findWithSubscriptionById(@Param("id") Long id);
}
