package com.example.portfolio.webstorespring.models.dtos.orders.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeliveryResponse(Long id,
                               String deliveryAddress,
                               String shipmentAddress,
                               @JsonProperty(value = "deliveryType")
                               DeliveryTypeResponse deliveryTypeResponse
) {
}
