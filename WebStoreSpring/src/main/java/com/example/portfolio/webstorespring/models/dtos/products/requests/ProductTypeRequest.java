package com.example.portfolio.webstorespring.models.dtos.products.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductTypeRequest(
        @NotBlank(message = "The product type name can't be blank")
        @Size(min = 3, max = 20, message = "The product type name must between min 3 and max 20 letters")
        String name) {
}
