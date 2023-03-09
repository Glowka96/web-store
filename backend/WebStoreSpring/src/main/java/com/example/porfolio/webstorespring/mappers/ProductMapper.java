package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.ProductDto;
import com.example.porfolio.webstorespring.model.entity.products.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ProducerMapper.class
        }
)
public interface ProductMapper {

    @Mapping(target = "subcategoryDto", source = "subcategory")
    @Mapping(target = "producerDto", source = "producer")
    ProductDto mapToDto(Product product);

    @Mapping(target = "subcategoryDto", source = "subcategory")
    @Mapping(target = "producerDto", source = "producer")
    List<ProductDto> mapToDto(List<Product> products);

    @Mapping(target = "subcategory", source = "subcategoryDto")
    @Mapping(target = "producer", source = "producerDto")
    Product mapToEntity(ProductDto productDto);
}
