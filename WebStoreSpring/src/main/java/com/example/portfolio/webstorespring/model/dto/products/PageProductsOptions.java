package com.example.portfolio.webstorespring.model.dto.products;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record PageProductsOptions(
        @Min(value = 0, message = "The page number must be greater than or equals to 0")
        Integer pageNo,
        @Min(value = 1, message = "The page size must be greater than or equals to 1")
        @Max(value = 48, message = "The page size must be less than or equals to 48")
        Integer size,
        @Pattern(regexp = "^([A-Za-z]*\\s-\\s[A-Za-z]*)$",
                message = "This is not valid sort option")
        String sortOption) {
}
