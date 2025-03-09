package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.products.ProductWithPromotionDTO;
import com.example.portfolio.webstorespring.models.dtos.products.requests.ProductRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProductResponse;
import com.example.portfolio.webstorespring.models.entities.products.Product;

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

    static ProductWithPromotionDTO mapToDtoWithPromotion(Product product) {
        return new ProductWithPromotionDTO(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getQuantity(),
                product.getSubcategory().getId(),
                product.getPrice(),
                product.getPromotions().isEmpty() ? null : product.getPromotions().iterator().next().getPromotionPrice(),
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
