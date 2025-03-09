package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.models.dtos.products.requests.SubcategoryRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.SubcategoryResponse;
import com.example.portfolio.webstorespring.models.entities.products.Subcategory;

public class SubcategoryBuilderHelper {

    private static final String NAME = "Test";

    public static Subcategory createSubcategory() {
        return Subcategory.builder()
                .id(1L)
                .name(NAME)
                .build();
    }

    public static Subcategory createSubcategoryWithoutId(String name) {
        return Subcategory.builder()
                .name(name)
                .build();
    }

    public static SubcategoryRequest createSubcategoryRequest() {
        return createSubcategoryRequest(NAME);
    }

    public static SubcategoryRequest createSubcategoryRequest(String name) {
        return new SubcategoryRequest(name);
    }

    public static SubcategoryResponse createSubcategoryResponse() {
        return new SubcategoryResponse(1L, NAME);
    }
}
