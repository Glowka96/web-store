package com.example.porfolio.webstorespring.model.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "The delivery address can't be null")
    @NotBlank(message = "The delivery address can't be blank")
    private String deliveryAddress;

    @JsonProperty("shipments")
    private List<ShipmentRequest> shipmentRequests;
}
