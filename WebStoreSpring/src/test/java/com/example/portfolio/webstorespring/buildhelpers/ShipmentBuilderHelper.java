package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.orders.request.ShipmentRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.ShipmentResponse;
import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.orders.Shipment;
import com.example.portfolio.webstorespring.model.entity.products.Product;

import java.math.BigDecimal;

import static com.example.portfolio.webstorespring.buildhelpers.ProductBuilderHelper.*;

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
        ProductRequest productRequest = createProductRequest();
        return ShipmentRequest.builder()
                .quantity(3)
                .productRequest(productRequest)
                .build();
    }

    public static ShipmentResponse createShipmentResponse() {
        ProductResponse productResponse = createProductResponse();
        return ShipmentResponse.builder()
                .id(1L)
                .price(BigDecimal.valueOf(60.0))
                .productResponse(productResponse)
                .quantity(3)
                .build();
    }
}
