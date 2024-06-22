package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;

public class ProductPricePromotionBuilderHelper {

    public static final Property<ProductPricePromotion, Long> ID = new Property<>();
    public static final Property<ProductPricePromotion, BigDecimal> PRICE = new Property<>();
    public static final Property<ProductPricePromotion, LocalDateTime> START_DATE = new Property<>();
    public static final Property<ProductPricePromotion, LocalDateTime> END_DATE = new Property<>();

    public static final Instantiator<ProductPricePromotion> BASIC_PROMOTION = lookup ->
            ProductPricePromotion.builder()
                    .id(lookup.valueOf(ID, 1L))
                    .promotionPrice(lookup.valueOf(PRICE, BigDecimal.valueOf(10.0)))
                    .startDate(lookup.valueOf(START_DATE, LOCAL_DATE_TIME))
                    .startDate(lookup.valueOf(END_DATE, LOCAL_DATE_TIME.plusDays(15)))
                    .build();

    public static ProductPricePromotionRequest createProductPricePromotionRequest() {
        return ProductPricePromotionRequest.builder()
                .promotionPrice(BigDecimal.valueOf(10.0))
                .startDate(LOCAL_DATE_TIME)
                .endDate(LOCAL_DATE_TIME.plusDays(15))
                .productId(1L)
                .build();
    }

    public static ProductPricePromotionRequest createProductPricePromotionRequest(BigDecimal price) {
        return ProductPricePromotionRequest.builder()
                .promotionPrice(price)
                .startDate(LOCAL_DATE_TIME)
                .endDate(LOCAL_DATE_TIME.plusDays(15))
                .productId(1L)
                .build();
    }

    public static ProductPricePromotionResponse createProductPricePromotionResponse() {
        return ProductPricePromotionResponse.builder()
                .id(1L)
                .promotionPrice(BigDecimal.valueOf(10.0))
                .startDate(LOCAL_DATE_TIME)
                .endDate(LOCAL_DATE_TIME.plusDays(15))
                .build();
    }
}
