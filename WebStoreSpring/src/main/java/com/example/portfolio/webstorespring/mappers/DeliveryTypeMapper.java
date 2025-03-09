package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.orders.requests.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.models.dtos.orders.responses.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.models.entities.orders.DeliveryType;

import java.util.List;


public interface DeliveryTypeMapper {

    static List<DeliveryTypeResponse> mapToResponse(List<DeliveryType> deliveryTypes) {
        return deliveryTypes.stream()
                .map(DeliveryTypeMapper::mapToResponse)
                .toList();
    }

    static DeliveryTypeResponse mapToResponse(DeliveryType deliveryType) {
        return new DeliveryTypeResponse(
                deliveryType.getId(),
                deliveryType.getName(),
                deliveryType.getPrice()
        );
    }

    static DeliveryType mapToEntity(DeliveryTypeRequest deliveryTypeRequest) {
        return DeliveryType.builder()
                .name(deliveryTypeRequest.name())
                .price(deliveryTypeRequest.price())
                .build();
    }
}
