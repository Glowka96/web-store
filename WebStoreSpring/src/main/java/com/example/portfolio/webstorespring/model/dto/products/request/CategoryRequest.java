package com.example.portfolio.webstorespring.model.dto.products.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "The category name can't be blank")
        @Size(min = 3, max = 20, message = "The category name must between min 3 and max 20 letters")
        String name) {
}
