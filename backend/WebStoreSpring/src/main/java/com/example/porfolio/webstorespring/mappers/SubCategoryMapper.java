package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.SubCategoryDto;
import com.example.porfolio.webstorespring.model.entity.products.SubCategory;
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
public interface SubCategoryMapper {

    @Mapping(target = "categoryDto", source = "category")
    @Mapping(target = "productsDto", source = "products")
    SubCategoryDto mapToDto(SubCategory subcategory);

    @Mapping(target = "category", source = "categoryDto")
    @Mapping(target = "products", source = "productsDto")
    SubCategory mapToEntity(SubCategoryDto subCategoryDto);

    List<SubCategoryDto> mapToDto(List<SubCategory> subCategories);
}
