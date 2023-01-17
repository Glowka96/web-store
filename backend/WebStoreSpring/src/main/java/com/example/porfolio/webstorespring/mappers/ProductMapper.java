package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.example.porfolio.webstorespring.model.entity.products.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface ProductMapper {

    @Mapping(target = "subCategoryDto", source = "subCategory")
    @Mapping(target = "producerDto", source = "producer")
    ProductDto productToProductDto(Product product);

    @Mapping(target = "subCategory", source = "subCategoryDto")
    @Mapping(target = "producer", source = "producerDto")
    Product productDtoToProduct(ProductDto productDto);
}
