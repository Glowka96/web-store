package com.example.portfolio.webstorespring.model.dto.orders.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Valid
    @JsonProperty("delivery")
    @NotNull(message = "The delivery can't be null")
    private DeliveryRequest deliveryRequest;

    @Valid
    @JsonProperty("shipments")
    @NotEmpty(message = "The shipments can't be empty")
    private List<ShipmentRequest> shipmentRequests;

    @Size(min=5, max = 10, message = "The discount code must between min 5 and max 10 letters")
    private String discountCode;
}
