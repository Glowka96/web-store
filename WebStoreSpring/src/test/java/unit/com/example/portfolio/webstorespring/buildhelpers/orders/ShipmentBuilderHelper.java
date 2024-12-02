package com.example.portfolio.webstorespring.buildhelpers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.ShipmentResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;

import java.math.BigDecimal;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.BASIC_PRODUCT;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithPromotionDtoBuildHelper.createProductWithPromotionDTO;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

public class ShipmentBuilderHelper {

    public static Shipment createShipment() {
        return Shipment.builder()
                .id(1L)
                .quantity(3)
                .product(make(a(BASIC_PRODUCT)))
                .build();
    }

    public static ShipmentRequest createShipmentRequest() {
        return new ShipmentRequest(
                1L,
                3
        );
    }

    public static ShipmentRequest createShipmentRequest(Long productId) {
        return new ShipmentRequest(
                productId,
                1
        );
    }

    public static ShipmentResponse createShipmentResponse() {
        return new ShipmentResponse(
                1L,
                createProductWithPromotionDTO(),
                3,
                BigDecimal.valueOf(60.0)
        );
    }
}
