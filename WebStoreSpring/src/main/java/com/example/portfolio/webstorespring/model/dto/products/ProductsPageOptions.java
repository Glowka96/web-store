package com.example.portfolio.webstorespring.model.dto.products;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record ProductsPageOptions(
        @Min(value = 0, message = "The page number must be 0 or greater")
        Integer pageNo,
        @Min(value = 1, message = "The page size must be between 1 and 48")
        @Max(value = 48, message = "The page size must be between 1 and 48")
        Integer size,
        @Pattern(regexp = "^([A-Za-z]+)\\s-\\s(asc|desc)$",
                message = "Sort option should be in the format 'field - direction' where direction is 'asc' or 'desc'")
        String sortOption) {
}
