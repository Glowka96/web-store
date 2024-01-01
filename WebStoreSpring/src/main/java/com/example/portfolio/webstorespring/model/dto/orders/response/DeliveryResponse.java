package com.example.portfolio.webstorespring.model.dto.orders.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeliveryResponse {

    private Long id;
    private String deliveryAddress;
    private String shipmentAddress;

    @JsonProperty(value = "deliveryType")
    private DeliveryTypeResponse deliveryTypeResponse;
}
