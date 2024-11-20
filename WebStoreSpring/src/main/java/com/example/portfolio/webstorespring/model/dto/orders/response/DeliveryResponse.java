package com.example.portfolio.webstorespring.model.dto.orders.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeliveryResponse(Long id,
                               String deliveryAddress,
                               String shipmentAddress,
                               @JsonProperty(value = "deliveryType")
                               DeliveryTypeResponse deliveryTypeResponse
) {
}
