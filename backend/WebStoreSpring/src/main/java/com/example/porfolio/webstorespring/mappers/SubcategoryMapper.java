package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.SubcategoryDto;
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

    @Mapping(target = "categoryDto", source = "category")
    @Mapping(target = "productsDto", source = "products")
    SubcategoryDto mapToDto(Subcategory subcategory);

    @Mapping(target = "category", source = "categoryDto")
    @Mapping(target = "products", source = "productsDto")
    Subcategory mapToEntity(SubcategoryDto subCategoryDto);

    List<SubcategoryDto> mapToDto(List<Subcategory> subcategories);
}
