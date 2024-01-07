package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;

public class CategoryBuilderHelper {

    public static Category createCategory() {
        return Category.builder()
                .id(1L)
                .name("Test")
                .build();
    }

    public static CategoryRequest createCategoryRequest(String name) {
        return CategoryRequest.builder()
                .name(name)
                .build();
    }

    public static CategoryResponse createCategoryResponse() {
        return CategoryResponse.builder()
                .id(1L)
                .name("Test")
                .build();
    }
}
