package com.example.portfolio.webstorespring.model.dto.products.response;

import com.example.portfolio.webstorespring.model.entity.products.Discount;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record DiscountUserResponse(
        BigDecimal discountRate,
        Set<Long> subcategoryIds
) {

    public static DiscountUserResponse mapToResponse(Discount discount) {
        return new DiscountUserResponse(discount.getDiscountRate(),
                discount.getSubcategories().stream().map(Subcategory::getId).collect(Collectors.toSet()));
    }
}
