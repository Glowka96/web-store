package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.SubcategoryRequest;
import com.example.porfolio.webstorespring.model.entity.products.Subcategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductMapper.class,
                CategoryMapper.class
        }
)
public interface SubcategoryMapper {

    SubcategoryRequest mapToDto(Subcategory subcategory);

    Subcategory mapToEntity(SubcategoryRequest subCategoryRequest);

    List<SubcategoryRequest> mapToDto(List<Subcategory> subcategories);
}
