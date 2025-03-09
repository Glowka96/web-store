package com.example.portfolio.webstorespring.models.dto.orders.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ShipmentRequest(
        @NotNull(message = "The product id cannot be null")
        Long productId,

        @NotNull(message = "The quantity cannot be null")
        @Min(value = 1, message = "The quantity must be greater than or equal to one")
        @Max(value = 1000, message = "The quantity must be less than or equal to thousand")
        Integer quantity
) {
}
