package com.example.portfolio.webstorespring.model.dto.orders.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private DeliveryRequest deliveryAddress;

    @JsonProperty("shipments")
    @NotEmpty(message = "The shipments can't be empty")
    private List<ShipmentRequest> shipmentRequests;
}
