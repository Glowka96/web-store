package com.example.portfolio.webstorespring.model.dto.orders;

import com.example.portfolio.webstorespring.model.dto.products.ProductRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentRequest {

    @NotNull(message = "The product can't be null")
    @JsonProperty("product")
    private ProductRequest productRequest;

    @NotNull(message = "The quantity can't be null")
    @Min(value = 1, message = "The quantity must be greater than or equal to one")
    @Max(value = 100, message = "The quantity must be less than or equal to hundred")
    private Integer quantity;

    @Digits(integer = 8, fraction = 2, message = "The price is invalid")
    private Double price;
}
