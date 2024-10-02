package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = {
                ProducerMapper.class,
                PromotionMapper.class,
                ProductTypeMapper.class
        }
)
public interface ProductMapper {

    @Mapping(target = "producerResponse", source = "producer")
    @Mapping(target = "pricePromotionsResponse", ignore = true)
    @Mapping(target = "productTypeResponse", source = "type")
    ProductResponse mapToDto(Product product);

    List<ProductResponse> mapToDto(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "promotions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "shipment", ignore = true)
    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "producer", ignore = true)
    @Mapping(target = "type", ignore = true)
    Product mapToEntity(ProductRequest productRequest);
}
