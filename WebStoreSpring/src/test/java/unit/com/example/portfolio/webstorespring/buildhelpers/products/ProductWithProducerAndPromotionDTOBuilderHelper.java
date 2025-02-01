package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.models.dto.products.ProductWithProducerAndPromotionDTO;

import java.math.BigDecimal;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;

public class ProductWithProducerAndPromotionDTOBuilderHelper {

    public static ProductWithProducerAndPromotionDTO createProductWithProducerAndPromotionDTO() {
        return new ProductWithProducerAndPromotionDTO(
                1L,
                "Test",
                "https://test.pl/test.jpg",
                10L,
                "Test product type name",
                1L,
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(15.0),
                LOCAL_DATE_TIME,
                "Test description",
                "Test producer name"
        );
    }

    public static ProductWithProducerAndPromotionDTO createNullProductWithProducerAndPromotionDTO(){
        return new ProductWithProducerAndPromotionDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
