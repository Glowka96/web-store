package com.example.portfolio.webstorespring.model.dto.orders.response;

import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private String nameUser;

    private BigDecimal totalPrice;

    @JsonProperty(value = "delivery")
    private DeliveryResponse deliveryResponse;

    private LocalDateTime createdAt;

    private OrderStatus status;

    @JsonProperty(value = "shipments")
    private List<ShipmentResponse> shipmentResponses;
}
