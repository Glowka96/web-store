package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.entity.orders.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private String nameUser;

    private Double productsPrice;

    private String deliveryAddress;

    private String shipmentAddress;

    private Date dateOfCreated;

    private OrderStatus status;

    @JsonProperty("shipments")
    private List<ShipmentResponse> shipmentResponses;
}
