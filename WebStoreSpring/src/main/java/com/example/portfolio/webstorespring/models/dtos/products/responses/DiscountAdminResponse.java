package com.example.portfolio.webstorespring.models.dtos.products.responses;

import com.example.portfolio.webstorespring.models.entities.products.Discount;
import com.example.portfolio.webstorespring.models.entities.products.Subcategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record DiscountAdminResponse(
        String code,
        BigDecimal discountRate,
        Long quantity,
        LocalDate endDate,
        Set<String> subcategoryNames
) {

    public static DiscountAdminResponse mapToResponse(Discount discount) {
        return new DiscountAdminResponse(
                discount.getCode(),
                discount.getDiscountRate(),
                discount.getQuantity(),
                discount.getEndDate(),
                discount.getSubcategories().stream().map(Subcategory::getName).collect(Collectors.toSet())
        );
    }
}
