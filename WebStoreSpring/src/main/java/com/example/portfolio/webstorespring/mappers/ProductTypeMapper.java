package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductTypeRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductTypeResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {

    ProductTypeResponse mapToDto(ProductType productType);

    List<ProductTypeResponse> mapToDto(List<ProductType> productTypes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductType mapToEntity(ProductTypeRequest productTypeRequest);
}
