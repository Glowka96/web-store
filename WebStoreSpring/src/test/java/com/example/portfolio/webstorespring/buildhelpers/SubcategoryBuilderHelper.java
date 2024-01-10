package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;

public class SubcategoryBuilderHelper {

    public static Subcategory createSubcategory() {
        return Subcategory.builder()
                .id(1L)
                .name("Test")
                .build();
    }

    public static SubcategoryRequest createSubcategoryRequest() {
        return SubcategoryRequest.builder()
                .name("Test")
                .build();
    }

    public static SubcategoryRequest createSubcategoryRequest(String name) {
        return SubcategoryRequest.builder()
                .name(name)
                .build();
    }

    public static SubcategoryResponse createSubcategoryResponse(){
        return SubcategoryResponse.builder()
                .id(1L)
                .name("Test")
                .build();
    }
}
