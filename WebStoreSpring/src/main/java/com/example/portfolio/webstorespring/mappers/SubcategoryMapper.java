package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.SubcategoryRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.SubcategoryResponse;
import com.example.portfolio.webstorespring.model.entity.products.Subcategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductMapper.class,
                CategoryMapper.class
        }
)
public interface SubcategoryMapper {

    @Mapping(target = "productResponses", ignore = true)
    SubcategoryResponse mapToDto(Subcategory subcategory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "category", ignore = true)
    Subcategory mapToEntity(SubcategoryRequest subCategoryRequest);

    List<SubcategoryResponse> mapToDto(List<Subcategory> subcategories);
}
