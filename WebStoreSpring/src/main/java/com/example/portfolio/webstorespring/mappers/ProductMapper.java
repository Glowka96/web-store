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
                ProductPricePromotionMapper.class
        }
)
public interface ProductMapper {

    @Mapping(target = "producerResponse", ignore = true)
    @Mapping(target = "pricePromotionsResponse", ignore = true)
    ProductResponse mapToDto(Product product);

    List<ProductResponse> mapToDto(List<Product> products);

    @Mapping(target = "pricePromotions", ignore = true)
    @Mapping(target = "dateOfCreation", ignore = true)
    @Mapping(target = "shipment", ignore = true)
    @Mapping(target = "subcategory", ignore = true)
    @Mapping(target = "producer", ignore = true)
    Product mapToEntity(ProductRequest productRequest);
}
