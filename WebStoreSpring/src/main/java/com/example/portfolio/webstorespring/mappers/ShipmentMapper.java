package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dto.orders.response.ShipmentResponse;
import com.example.portfolio.webstorespring.models.entity.orders.Shipment;

import java.util.List;

public interface ShipmentMapper {

    static List<ShipmentResponse> mapToDto(List<Shipment> shipments) {
        return shipments.stream()
                .map(ShipmentMapper::mapToDto)
                .toList();
    }

    private static ShipmentResponse mapToDto(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                ProductMapper.mapToDtoWithPromotion(shipment.getProduct()),
                shipment.getQuantity(),
                shipment.getPrice()
        );
    }
}
