package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
