package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByAccountId(Long accountId);

    Optional<Order> findOrderByAccountIdAndId(Long accountId, Long orderId);
}
