package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Delivery;

public interface DeliveryMapper {

    static DeliveryResponse mapToDto(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getDeliveryAddress(),
                delivery.getShipmentAddress(),
                DeliveryTypeMapper.mapToDto(delivery.getDeliveryType())
        );
    }
}
