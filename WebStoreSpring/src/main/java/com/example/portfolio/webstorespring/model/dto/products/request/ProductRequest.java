package com.example.portfolio.webstorespring.model.dto.products.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "The name can't be blank")
        @Size(min = 3, max = 32, message = "The name must between min 3 and max 32 letters")
        String name,

        @NotBlank(message = "The description can't be blank")
        @Size(min = 3, max = 512, message = "The description must between min 3 and max 512 letters")
        String description,

        @NotBlank(message = "The image url can't be blank")
        @Pattern(regexp = "^(https?://)?[\\w./\\s-]*\\.(?:jpg|png)$",
                message = "This is not image url")
        String imageUrl,

        @DecimalMin(value = "0.01", message = "The price must be greater than or equal to 0.01")
        @DecimalMax(value = "99999.99", message = "The price must be less than or equal to 99999.99")
        BigDecimal price,

        @Min(value = 1, message = "The quantity must be greater than or equal to one")
        @Max(value = 10_000, message = "The quantity must be less than or equal to 10000")
        Long quantity,

        @Positive(message = "The product type id must be positive number")
        @NotNull(message = "The product type id can't be null")
        Long productTypeId) {
}
