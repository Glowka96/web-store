package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;

import java.math.BigDecimal;
import java.util.Date;

public class ProductPricePromotionBuilderHelper {

    public static ProductPricePromotion createProductPricePromotion() {
        return ProductPricePromotion.builder()
                .id(1L)
                .promotionPrice(BigDecimal.valueOf(10.0))
                .startDate(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.toInstant()))
                .endDate(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.plusDays(15).toInstant()))
                .build();
    }

    public static ProductPricePromotionRequest createProductPricePromotionRequest() {
        return ProductPricePromotionRequest.builder()
                .promotionPrice(BigDecimal.valueOf(10.0))
                .startDate(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.toInstant()))
                .endDate(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.plusDays(15).toInstant()))
                .productId(1L)
                .build();
    }

    public static ProductPricePromotionRequest createProductPricePromotionRequest(BigDecimal price) {
        return ProductPricePromotionRequest.builder()
                .promotionPrice(price)
                .startDate(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.toInstant()))
                .endDate(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.plusDays(15).toInstant()))
                .productId(1L)
                .build();
    }

    public static ProductPricePromotionResponse createProductPricePromotionResponse() {
        return ProductPricePromotionResponse.builder()
                .id(1L)
                .promotionPrice(BigDecimal.valueOf(10.0))
                .startDate(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.toInstant()))
                .endDate(Date.from(DateForTestBuilderHelper.ZONED_DATE_TIME.plusDays(15).toInstant()))
                .build();
    }
}
