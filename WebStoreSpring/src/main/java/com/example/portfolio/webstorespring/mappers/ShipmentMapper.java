package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.orders.responses.ShipmentResponse;
import com.example.portfolio.webstorespring.models.entities.orders.Shipment;

import java.util.List;

public interface ShipmentMapper {

    static List<ShipmentResponse> mapToResponse(List<Shipment> shipments) {
        return shipments.stream()
                .map(ShipmentMapper::mapToResponse)
                .toList();
    }

    private static ShipmentResponse mapToResponse(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                ProductMapper.mapToDtoWithPromotion(shipment.getProduct()),
                shipment.getQuantity(),
                shipment.getPrice()
        );
    }
}
