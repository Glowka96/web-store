package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.models.dtos.products.requests.DiscountRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.DiscountAdminResponse;
import com.example.portfolio.webstorespring.models.dtos.products.responses.DiscountUserResponse;
import com.example.portfolio.webstorespring.models.entities.products.Discount;
import com.example.portfolio.webstorespring.models.entities.products.Subcategory;

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

    public static DiscountUserResponse createDiscountUserResponse() {
        return new DiscountUserResponse(
                BigDecimal.valueOf(0.1),
                Set.of(1L, 2L)
        );
    }

    public static DiscountAdminResponse createDiscountAdminResponse() {
        return new DiscountAdminResponse(
                "test01",
                BigDecimal.valueOf(0.1),
                1L,
                null,
                Set.of("Puzzle")
        );
    }
}
