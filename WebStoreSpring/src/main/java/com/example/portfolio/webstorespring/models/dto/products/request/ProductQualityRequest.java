package com.example.portfolio.webstorespring.models.dto.products.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ProductQualityRequest(
        @Positive(message = "The product id must be a positive number.")
        Long productId,

        @Min(value = 1, message = "The quantity must be greater than or equal to one")
        @Max(value = 10_000, message = "The quantity must be less than or equal to 10000")
        Long quantity) {
}
