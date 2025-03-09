package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.orders.responses.DeliveryResponse;
import com.example.portfolio.webstorespring.models.entities.orders.Delivery;

public interface DeliveryMapper {

    static DeliveryResponse mapToResponse(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getDeliveryAddress(),
                delivery.getShipmentAddress(),
                DeliveryTypeMapper.mapToResponse(delivery.getDeliveryType())
        );
    }

    static DeliveryResponse mapToResponseWithoutDeliveryType(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getDeliveryAddress(),
                delivery.getShipmentAddress(),
                null
        );
    }
}
