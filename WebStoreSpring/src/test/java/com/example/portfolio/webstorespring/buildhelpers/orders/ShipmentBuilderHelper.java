package com.example.portfolio.webstorespring.buildhelpers.orders;

import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.ShipmentResponse;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;

import java.math.BigDecimal;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.createProduct;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithPromotionDtoBuildHelper.createProductWithPromotionDTO;

public class ShipmentBuilderHelper {

    public static Shipment createShipment() {
        Product product = createProduct();
        return Shipment.builder()
                .id(1L)
                .quantity(3)
                .product(product)
                .build();
    }

    public static ShipmentRequest createShipmentRequest() {
        ProductWithPromotionDTO productRequest = createProductWithPromotionDTO();
        return ShipmentRequest.builder()
                .quantity(3)
                .product(productRequest)
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
