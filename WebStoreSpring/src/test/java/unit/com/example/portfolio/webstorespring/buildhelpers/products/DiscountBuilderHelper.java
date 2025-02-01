package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.models.dto.products.request.DiscountRequest;
import com.example.portfolio.webstorespring.models.entity.products.Discount;
import com.example.portfolio.webstorespring.models.entity.products.Subcategory;

import java.math.BigDecimal;
import java.util.Set;

public class DiscountBuilderHelper {

    public static Discount createDiscount() {
        return createDiscount(SubcategoryBuilderHelper.createSubcategory());
    }

    public static Discount createDiscount(Subcategory subcategory) {
        return Discount.builder()
                .id(1L)
                .code("test01")
                .discountRate(BigDecimal.valueOf(0.1))
                .quantity(1L)
                .subcategories(Set.of(subcategory))
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
