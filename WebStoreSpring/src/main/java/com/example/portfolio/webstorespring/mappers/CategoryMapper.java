package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.CategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.CategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;

import java.util.List;


public interface CategoryMapper {

    static List<CategoryResponse> mapToDto(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::mapToDto)
                .toList();
    }

    static CategoryResponse mapToDto(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSubcategories() == null ? null
                        : SubcategoryMapper.mapToDto(category.getSubcategories())
        );
    }

    static Category mapToEntity(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name())
                .build();
    }
}
