package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.model.entity.subscribers.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriberRepository<T extends Subscriber> extends JpaRepository<T, Long> {

    Optional<T> findByEmail(String email);

    Boolean existsByEmail(String email);

}
