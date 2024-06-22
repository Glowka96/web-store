package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;

import java.math.BigDecimal;

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
}
