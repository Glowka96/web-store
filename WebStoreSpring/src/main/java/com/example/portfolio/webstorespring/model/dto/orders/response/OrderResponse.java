package com.example.portfolio.webstorespring.model.dto.orders.response;

import com.example.portfolio.webstorespring.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private String nameUser;

    private BigDecimal totalPrice;

    @JsonProperty(value = "delivery")
    private DeliveryResponse deliveryResponse;

    private Date dateOfCreation;

    private OrderStatus status;

    @JsonProperty(value = "shipments")
    private List<ShipmentResponse> shipmentResponses;
}
