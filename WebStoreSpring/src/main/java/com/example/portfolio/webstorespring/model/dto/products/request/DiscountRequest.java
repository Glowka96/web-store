package com.example.portfolio.webstorespring.model.dto.products.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record DiscountRequest(
        @Pattern(regexp = "^[a-zA-Z0-9]{5,10}$", message = "The code must be between 5 and 10 letters or digits")
        String code,
        @DecimalMin(value = "0.1", message = "The discount rate must be greater than or equal to 0.1")
        @DecimalMax(value = "0.90", message = "The discount rate must be less than or equal to 0.90")
        BigDecimal discountRate,

        @Min(value = 1, message = "The quantity must be greater than or equal to one")
        @Max(value = 10_000, message = "The quantity must be less than or equal to 10000")
        Long quantity,

        LocalDate endDate,

        @NotEmpty(message = "The list of subcategory names can't be empty")
        Set<String> subcategoryNames
) {
}
