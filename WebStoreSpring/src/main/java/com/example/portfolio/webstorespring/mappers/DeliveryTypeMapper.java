package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;

import java.util.List;


public interface DeliveryTypeMapper {

    static List<DeliveryTypeResponse> mapToDto(List<DeliveryType> deliveryTypes) {
        return deliveryTypes.stream()
                .map(DeliveryTypeMapper::mapToDto)
                .toList();
    }

    static DeliveryTypeResponse mapToDto(DeliveryType deliveryType) {
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
