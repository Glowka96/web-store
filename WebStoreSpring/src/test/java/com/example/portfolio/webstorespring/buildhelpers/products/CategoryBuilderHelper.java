package com.example.portfolio.webstorespring.buildhelpers.products;

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

    public static Category createCategory(String name) {
        return Category.builder()
                .id(1L)
                .name(name)
                .build();
    }

    public static CategoryRequest createCategoryRequest() {
        return CategoryRequest.builder()
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
