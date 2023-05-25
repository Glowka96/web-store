package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.CategoryRequest;
import com.example.porfolio.webstorespring.model.dto.products.CategoryResponse;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface CategoryMapper {

    @Mapping(target = "subcategoryResponses", source = "subcategories")
    CategoryResponse mapToDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    Category mapToEntity(CategoryRequest categoryRequest);

    List<CategoryResponse> mapToDto(List<Category> categories);
}
