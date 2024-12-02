package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Category;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;

import java.util.List;

public interface SubcategoryMapper {

    static List<SubcategoryResponse> mapToDto(List<Subcategory> subcategories) {
        return subcategories.stream()
                .map(SubcategoryMapper::mapToDto)
                .toList();
    }

     static SubcategoryResponse mapToDto(Subcategory subcategory) {
        return new SubcategoryResponse(subcategory.getId(), subcategory.getName());

    }

    static Subcategory mapToEntity(SubcategoryRequest subcategoryRequest, Category category) {
        return Subcategory.builder()
                .name(subcategoryRequest.name())
                .category(category)
                .build();
    }
}
