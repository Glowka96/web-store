package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.model.dto.products.request.PromotionRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.model.entity.products.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
                ProductMapper.class
        })
public interface PromotionMapper {

    @Mapping(target = "productResponse", source = "product")
    PromotionResponse mapToDto(Promotion pricePromotion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    Promotion mapToEntity(PromotionRequest promotionRequest);
}
