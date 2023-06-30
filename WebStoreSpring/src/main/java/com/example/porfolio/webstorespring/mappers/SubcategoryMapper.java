package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.SubcategoryRequest;
import com.example.porfolio.webstorespring.model.dto.products.SubcategoryResponse;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
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
