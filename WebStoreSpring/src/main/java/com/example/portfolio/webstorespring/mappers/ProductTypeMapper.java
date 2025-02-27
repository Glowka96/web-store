package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;

import java.util.List;

public interface ProductTypeMapper {

    static List<ProductTypeResponse> mapToDto(List<ProductType> productTypes) {
        return productTypes.stream()
                .map(ProductTypeMapper::mapToDto)
                .toList();
    }

    static ProductTypeResponse mapToDto(ProductType productType) {
        return new ProductTypeResponse(productType.getId(), productType.getName());
    }

    //    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "products", ignore = true)
    static ProductType mapToEntity(ProductTypeRequest productTypeRequest){
        return ProductType.builder()
                .name(productTypeRequest.name())
                .build();
    }
}
