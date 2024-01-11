package com.example.portfolio.webstorespring.model.dto.orders.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {

    private Long id;
    private String deliveryAddress;
    private String shipmentAddress;

    @JsonProperty(value = "deliveryType")
    private DeliveryTypeResponse deliveryTypeResponse;
}
