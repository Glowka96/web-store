package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.DATE_OF_CREATED;

public class ProductWithPromotionDtoBuildHelper {

    public static ProductWithPromotionDTO createProductWithPromotionDTO() {
        return new ProductWithPromotionDTO(
                1L,
                "Test",
                "https://test.pl/test.jpg",
                10L,
                "Test product type name",
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(15.0),
                Timestamp.valueOf(DATE_OF_CREATED)
        );
    }
}