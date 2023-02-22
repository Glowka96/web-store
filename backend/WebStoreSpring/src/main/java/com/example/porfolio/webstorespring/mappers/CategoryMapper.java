package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.CategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface CategoryMapper {

    @Mapping(target = "subcategoriesDto", source = "subcategories")
    CategoryDto mapToDto(Category category);

    @Mapping(target = "subcategories", source = "subcategoriesDto")
    Category mapToEntity(CategoryDto categoryDto);

    List<CategoryDto> mapToDto(List<Category> categories);
}
