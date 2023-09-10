package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
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

    @Mapping(target = "shipment", ignore = true)
    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "producer", ignore = true)
    Product mapToEntity(ProductRequest productRequest);
}
