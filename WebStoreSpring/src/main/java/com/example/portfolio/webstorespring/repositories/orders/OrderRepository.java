package com.example.portfolio.webstorespring.repositories.orders;

import com.example.portfolio.webstorespring.model.entity.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByAccountId(Long accountId);

    @Query("""
        SELECT o FROM Order o
        WHERE o.account.id = :accountId
        ORDER BY o.dateOfCreation DESC
        LIMIT 5
""")
    List<Order> findLastFiveAccountOrder(@Param(value = "accountId") Long accountId);

}
