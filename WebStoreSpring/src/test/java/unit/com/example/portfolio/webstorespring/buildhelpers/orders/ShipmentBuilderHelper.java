package com.example.portfolio.webstorespring.buildhelpers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.ShipmentResponse;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;

import java.math.BigDecimal;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.BASIC_PRODUCT;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithPromotionDtoBuildHelper.createProductWithPromotionDTO;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

public class ShipmentBuilderHelper {

    public static Shipment createShipment() {
        Product product = make(a(BASIC_PRODUCT));
        return Shipment.builder()
                .id(1L)
                .quantity(3)
                .product(product)
                .build();
    }

    public static ShipmentRequest createShipmentRequest() {
        return ShipmentRequest.builder()
                .quantity(3)
                .productId(1L)
                .build();
    }

    public static ShipmentRequest createShipmentRequest(Long productId) {
        return ShipmentRequest.builder()
                .quantity(1)
                .productId(productId)
                .build();
    }

    public static ShipmentResponse createShipmentResponse() {
        ProductWithPromotionDTO product = createProductWithPromotionDTO();
        return ShipmentResponse.builder()
                .id(1L)
                .price(BigDecimal.valueOf(60.0))
                .product(product)
                .quantity(3)
                .build();
    }
}
