package com.example.portfolio.webstorespring.models.dto.orders.response;

import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long id,
                            String nameUser,
                            BigDecimal totalPrice,
                            @JsonProperty(value = "delivery")
                            DeliveryResponse deliveryResponse,
                            LocalDateTime createdAt,
                            OrderStatus status,
                            @JsonProperty(value = "shipments")
                            List<ShipmentResponse> shipmentResponses
) {
}
