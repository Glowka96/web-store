package com.example.portfolio.webstorespring.repositories.orders;

import com.example.portfolio.webstorespring.model.entity.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("""
                    SELECT o FROM Order o
                    LEFT JOIN FETCH o.delivery d
                    LEFT JOIN FETCH d.deliveryType
                    LEFT JOIN FETCH o.shipments s
                    LEFT JOIN FETCH s.product
                    WHERE o.id = :orderId
            """)
    Optional<Order> findById(@Param("orderId") Long orderId);

    @Query("""
                    SELECT o FROM Order o
                    LEFT JOIN FETCH o.delivery
                    WHERE o.account.id =:accountId
                    ORDER BY o.createdAt DESC
            """)
    List<Order> findAllByAccountId(@Param("accountId") Long accountId);

    @Query("""
                    SELECT o FROM Order o
                    LEFT JOIN FETCH o.delivery
                    WHERE o.account.id = :accountId
                    ORDER BY o.createdAt DESC
                    LIMIT 5
            """)
    List<Order> findLastFiveAccountOrder(@Param("accountId") Long accountId);

}
