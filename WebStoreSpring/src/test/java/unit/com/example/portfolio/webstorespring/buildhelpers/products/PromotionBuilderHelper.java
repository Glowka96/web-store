package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.PromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Promotion;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;

public class PromotionBuilderHelper {

    public static final Property<Promotion, Long> ID = new Property<>();
    public static final Property<Promotion, BigDecimal> PRICE = new Property<>();
    public static final Property<Promotion, LocalDateTime> START_DATE = new Property<>();
    public static final Property<Promotion, LocalDateTime> END_DATE = new Property<>();

    public static final Instantiator<Promotion> BASIC_PROMOTION = lookup ->
            Promotion.builder()
                    .id(lookup.valueOf(ID, 1L))
                    .promotionPrice(lookup.valueOf(PRICE, BigDecimal.valueOf(10.0)))
                    .startDate(lookup.valueOf(START_DATE, LOCAL_DATE_TIME))
                    .endDate(lookup.valueOf(END_DATE, LOCAL_DATE_TIME.plusDays(15)))
                    .build();

    public static PromotionRequest createPromotionRequest() {
        return createPromotionRequest(BigDecimal.valueOf(10.0));
    }

    public static PromotionRequest createPromotionRequest(BigDecimal price) {
        return new PromotionRequest(
                1L,
                price,
                LOCAL_DATE_TIME,
                LOCAL_DATE_TIME.plusDays(15)
        );
    }

    public static PromotionResponse createPromotionResponse() {
        return new PromotionResponse(
                1L,
                null,
                BigDecimal.valueOf(10.0),
                LOCAL_DATE_TIME,
                LOCAL_DATE_TIME.plusDays(15)
        );
    }
}
