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

    public static ProductTypeRequest createProductTypeRequest() {
        return ProductTypeRequest.builder()
                .name("Test type")
                .build();
    }

    public static ProductTypeRequest createProductTypeRequest(String name) {
        return ProductTypeRequest.builder()
                .name(name)
                .build();
    }

    public static ProductTypeResponse createProductTypeResponse() {
        return ProductTypeResponse.builder()
                .id(1L)
                .name("Test type")
                .build();
    }
}
