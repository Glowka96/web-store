package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;

import java.math.BigDecimal;
import java.time.Clock;

public class ProductWithPromotionDtoBuildHelper {

    public static ProductWithPromotionDTO createProductWithPromotionDTO() {
        return new ProductWithPromotionDTO(
                1L,
                "Test",
                "https://test.pl/test.jpg",
                10L,
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(15.0)
        );
    }

    public static ProductWithPromotionDTO createProductWithPromotionDTO(Clock clock) {
        return new ProductWithPromotionDTO(
                1L,
                "Test",
                "test.pl/test.png",
                1L,
                BigDecimal.valueOf(100L),
                BigDecimal.valueOf(90L),
                BigDecimal.valueOf(70L)
        );
    }
}
