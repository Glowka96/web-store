package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.products.requests.ProductTypeRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProductTypeResponse;
import com.example.portfolio.webstorespring.models.entities.products.ProductType;

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

    static ProductType mapToEntity(ProductTypeRequest productTypeRequest){
        return ProductType.builder()
                .name(productTypeRequest.name())
                .build();
    }
}
