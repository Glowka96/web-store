package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductResponse;
import com.example.portfolio.webstorespring.model.entity.products.Product;

import java.util.List;

public interface ProductMapper {

    static List<ProductResponse> mapToDto(List<Product> products) {
        return products.stream()
                .map(ProductMapper::mapToDto)
                .toList();
    }

    static ProductResponse mapToDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                product.getQuantity(),
                ProductTypeMapper.mapToDto(product.getType()),
                ProducerMapper.mapToDto(product.getProducer()),
                null
        );
    }

    static Product mapToEntity(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .imageUrl(productRequest.imageUrl())
                .price(productRequest.price())
                .quantity(productRequest.quantity())
                .build();
    }
}
