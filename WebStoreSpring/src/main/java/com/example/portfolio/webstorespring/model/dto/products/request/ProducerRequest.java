package com.example.portfolio.webstorespring.model.dto.products.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProducerRequest(
        @NotBlank(message = "The producer name can't be blank")
        @Size(min = 3, max = 20, message = "The producer name must between min 3 and max 20 letters")
        String name
) {
}
