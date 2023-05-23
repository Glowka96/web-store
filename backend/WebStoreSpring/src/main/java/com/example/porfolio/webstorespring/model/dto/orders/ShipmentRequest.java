package com.example.porfolio.webstorespring.model.dto.orders;

import com.example.porfolio.webstorespring.model.dto.products.ProductRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentRequest {

    @NotNull(message = "The product can't be null")
    @JsonProperty("product")
    private ProductRequest productRequest;

    private Integer quantity;

    private Double price;
}
