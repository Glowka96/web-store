package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithProducerAndPromotionDTO;

import java.math.BigDecimal;
import java.util.Date;

public class ProductWithProducerAndPromotionDTOBuilderHelper {

    public static ProductWithProducerAndPromotionDTO createProductWithProducerAndPromotionDTO() {
        return new ProductWithProducerAndPromotionDTO(
                1L,
                "Test",
                "https://test.pl/test.jpg",
                10L,
                "Test product type name",
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(15.0),
                Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.toInstant()),
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
                null
        );
    }
}
