package com.example.portfolio.webstorespring.model.dto.orders.request;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {

    @Valid
    @NotNull(message = "The product can't be null")
    @JsonProperty(value = "product")
    private ProductRequest productRequest;

    @NotNull(message = "The quantity can't be null")
    @Min(value = 1, message = "The quantity must be greater than or equal to one")
    @Max(value = 100, message = "The quantity must be less than or equal to hundred")
    private Integer quantity;
}
