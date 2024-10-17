package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.DiscountRequest;
import com.example.portfolio.webstorespring.model.entity.products.Discount;

import java.math.BigDecimal;
import java.util.Set;

public class DiscountBuilderHelper {

    public static Discount createDiscount() {
        return Discount.builder()
                .id(1L)
                .code("test01")
                .discountRate(BigDecimal.valueOf(0.1))
                .quantity(1L)
                .subcategories(Set.of(SubcategoryBuilderHelper.createSubcategory()))
                .build();
    }

    public static DiscountRequest createDiscountRequestWithoutCode() {
        return new DiscountRequest(
                null,
                BigDecimal.valueOf(0.1),
                1L,
                null,
                Set.of("Test")
        );
    }

    public static DiscountRequest createDiscountRequestWithCode() {
        return new DiscountRequest(
                "test01",
                BigDecimal.valueOf(0.1),
                1L,
                null,
                Set.of("Test")
        );
    }
}
