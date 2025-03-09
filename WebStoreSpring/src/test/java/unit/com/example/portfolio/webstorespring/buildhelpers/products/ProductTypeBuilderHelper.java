package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.models.dtos.products.requests.ProductTypeRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProductTypeResponse;
import com.example.portfolio.webstorespring.models.entities.products.ProductType;

public class ProductTypeBuilderHelper {

    public static ProductType createProductType() {
        return ProductType.builder()
                .id(1L)
                .name("Test type")
                .build();
    }

    public static ProductType createProductTypeWithoutId(String name) {
        return ProductType.builder()
                .name(name)
                .build();
    }

    public static ProductTypeRequest createProductTypeRequest() {
        return createProductTypeRequest("Test type");
    }

    public static ProductTypeRequest createProductTypeRequest(String name) {
        return new ProductTypeRequest(name);
    }

    public static ProductTypeResponse createProductTypeResponse() {
        return new ProductTypeResponse(
                1L,
                "Test type"
        );
    }
}
