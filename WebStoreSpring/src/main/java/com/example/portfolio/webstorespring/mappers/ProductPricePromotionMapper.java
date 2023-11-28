package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.ProductPricePromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProductPricePromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.ProductPricePromotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductPricePromotionMapper {

    @Mapping(target = "productResponse", ignore = true)
    ProductPricePromotionResponse mapToDto(ProductPricePromotion pricePromotion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductPricePromotion mapToEntity(ProductPricePromotionRequest promotionRequest);
}
