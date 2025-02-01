package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.models.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.models.entity.products.Category;

public class CategoryBuilderHelper {
    private static final String NAME = "Test";

    public static Category createCategory() {
        return Category.builder()
                .id(1L)
                .name(NAME)
                .build();
    }

    public static CategoryRequest createCategoryRequest() {
        return createCategoryRequest(NAME);
    }

    public static CategoryRequest createCategoryRequest(String name) {
        return new CategoryRequest(name);
    }

    public static CategoryResponse createCategoryResponse() {
        return new CategoryResponse(1L, NAME, null);
    }
}
