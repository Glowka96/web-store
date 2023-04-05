package com.example.porfolio.webstorespring.repositories.orders;

import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
