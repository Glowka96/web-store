package com.example.portfolio.webstorespring.models.dtos.orders.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DeliveryTypeRequest(
        @NotBlank(message = "The delivery type name can't be blank")
        @Size(min = 3, max = 32, message = "The delivery type name must between min 3 and max 32 letters")
        String name,

        @DecimalMin(value = "0.00", message = "The price must be greater than or equal to 0.00")
        @DecimalMax(value = "999.99", message = "The price must be less than or equal to 999.99")
        BigDecimal price
) {
}
