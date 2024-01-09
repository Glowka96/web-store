package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ProductPricePromotionBuilderHelper {

    private static final LocalDateTime DATE_OF_CREATED = LocalDateTime.of(
            2024,
            1,
            10,
            20,
            20,
            0,
            0);

    public static ProductPricePromotion createProductPricePromotion() {
        return ProductPricePromotion.builder()
                .id(1L)
                .promotionPrice(BigDecimal.valueOf(10.0))
                .startDate(Timestamp.valueOf(DATE_OF_CREATED))
                .endDate(Timestamp.valueOf(DATE_OF_CREATED.plusDays(15)))
                .build();
    }

    public static ProductPricePromotionRequest createProductPricePromotionRequest() {
        return ProductPricePromotionRequest.builder()
                .promotionPrice(BigDecimal.valueOf(10.0))
                .startDate(Timestamp.valueOf(DATE_OF_CREATED))
                .endDate(Timestamp.valueOf(DATE_OF_CREATED.plusDays(15)))
                .productId(1L)
                .build();
    }

    public static ProductPricePromotionRequest createProductPricePromotionRequest(BigDecimal price) {
        return ProductPricePromotionRequest.builder()
                .promotionPrice(price)
                .startDate(Timestamp.valueOf(DATE_OF_CREATED))
                .endDate(Timestamp.valueOf(DATE_OF_CREATED.plusDays(15)))
                .productId(1L)
                .build();
    }
}
