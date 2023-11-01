package com.example.portfolio.webstorespring.model.dto.orders.request;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {

    @NotNull(message = "The product can't be null")
    @JsonProperty(value = "product")
    private ProductRequest productRequest;

    @NotNull(message = "The quantity can't be null")
    @Min(value = 1, message = "The quantity must be greater than or equal to one")
    @Max(value = 100, message = "The quantity must be less than or equal to hundred")
    private Integer quantity;

    @DecimalMin(value = "0.01", message = "The price must be greater than or equal to 0.01")
    @DecimalMax(value = "99999999.99", message = "The price must be less than or equal to 99999999.99")
    private BigDecimal price;
}
