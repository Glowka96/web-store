package com.example.portfolio.webstorespring.repositories.orders;

import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Long> {
}
