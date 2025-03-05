package com.example.portfolio.webstorespring.models.dto.products.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubcategoryRequest(
        @NotBlank(message = "The name can't be blank")
        @Size(min = 3, max = 20, message = "The name must between min 3 and max 20 letters")
        String name) {
}
