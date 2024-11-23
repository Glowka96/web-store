package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;

import java.util.List;

public interface SubcategoryMapper {

     static SubcategoryResponse mapToDto(Subcategory subcategory) {
        return new SubcategoryResponse(subcategory.getId(), subcategory.getName());

    }

     static List<SubcategoryResponse> mapToDto(List<Subcategory> subcategories) {
        return subcategories.stream()
                .map(SubcategoryMapper::mapToDto)
                .toList();

    }
}
