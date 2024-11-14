package com.example.portfolio.webstorespring.model.dto.orders.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {

    @NotNull(message = "The product id cannot be null")
    @Positive(message = "The product id must be positive number")
    private Long productId;

    @NotNull(message = "The quantity cannot be null")
    @Min(value = 1, message = "The quantity must be greater than or equal to one")
    @Max(value = 1000, message = "The quantity must be less than or equal to thousand")
    private Integer quantity;
}
