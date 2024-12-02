package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;

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
