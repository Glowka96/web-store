package com.example.porfolio.webstorespring.mappers;

import com.example.porfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.porfolio.webstorespring.model.dto.products.ProductResponse;
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

    @Mapping(target = "producerResponse", source = "producer")
    ProductResponse mapToDto(Product product);

    @Mapping(target = "producerResponse", source = "producer")
    List<ProductResponse> mapToDto(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "producer", ignore = true)
    Product mapToEntity(ProductRequest productRequest);
}
