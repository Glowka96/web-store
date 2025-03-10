package com.example.portfolio.webstorespring.models.dtos.products.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductQualityRequest(
        @NotNull(message = "The product id can't be null")
        Long productId,

        @Min(value = 1, message = "The quantity must be greater than or equal to one")
        @Max(value = 10_000, message = "The quantity must be less than or equal to 10000")
        Long quantity) {
}
