package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.products.requests.CategoryRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.CategoryResponse;
import com.example.portfolio.webstorespring.models.entities.products.Category;

import java.util.List;


public interface CategoryMapper {

    static List<CategoryResponse> mapToResponse(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::mapToResponse)
                .toList();
    }

    static CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSubcategories() == null ? null
                        : SubcategoryMapper.mapToResponse(category.getSubcategories())
        );
    }

    static Category mapToEntity(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name())
                .build();
    }
}
