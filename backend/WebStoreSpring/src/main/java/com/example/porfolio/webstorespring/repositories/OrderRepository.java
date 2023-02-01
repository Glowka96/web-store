package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
