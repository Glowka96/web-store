package com.example.portfolio.webstorespring.model.dto.orders.response;

public record DeliveryResponseWithoutDeliveryType(Long id,
                                                  String deliveryAddress,
                                                  String shipmentAddress) {
}
