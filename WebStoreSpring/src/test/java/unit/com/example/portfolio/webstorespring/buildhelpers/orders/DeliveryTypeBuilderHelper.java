package com.example.portfolio.webstorespring.buildhelpers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;

import java.math.BigDecimal;

public class DeliveryTypeBuilderHelper {
    private static final String TYPE = "Inpost - Standard delivery";

    public static DeliveryType createDeliveryType() {
        return DeliveryType.builder()
                .id(1L)
                .name(TYPE)
                .price(BigDecimal.valueOf(10.0))
                .build();
    }

    public static DeliveryTypeRequest createDeliveryTypeRequest() {
        return createDeliveryTypeRequest(TYPE, BigDecimal.valueOf(10.0));
    }

    public static DeliveryTypeRequest createDeliveryTypeRequest(String type, BigDecimal price) {
        return new DeliveryTypeRequest(type, price);
    }

    public static DeliveryTypeResponse createDeliveryTypeResponse() {
        return new DeliveryTypeResponse(
                1L,
                TYPE,
                BigDecimal.valueOf(10.0)
        );
    }
}
