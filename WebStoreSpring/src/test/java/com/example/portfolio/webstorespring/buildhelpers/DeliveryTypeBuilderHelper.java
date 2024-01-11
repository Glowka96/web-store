package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;

import java.math.BigDecimal;

public class DeliveryTypeBuilderHelper {
    private static final String TYPE = "Inpost - Standard delivery";

    public static DeliveryType createDeliveryType() {
        return DeliveryType.builder()
                .id(1L)
                .type(TYPE)
                .price(BigDecimal.valueOf(10.0))
                .build();
    }

    public static DeliveryTypeRequest createDeliveryTypeRequest() {
        return DeliveryTypeRequest.builder()
                .type(TYPE)
                .price(BigDecimal.valueOf(10.0))
                .build();
    }


    public static DeliveryTypeRequest createDeliveryTypeRequest(String type, BigDecimal price) {
        return DeliveryTypeRequest.builder()
                .type(type)
                .price(price)
                .build();
    }
    
    public static DeliveryTypeResponse createDeliveryTypeResponse() {
        return DeliveryTypeResponse.builder()
                .id(1L)
                .type(TYPE)
                .price(BigDecimal.valueOf(10.0))
                .build();
    }
}
