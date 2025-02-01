package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dto.products.request.PromotionRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.PromotionResponse;
import com.example.portfolio.webstorespring.models.entity.products.Promotion;

public interface PromotionMapper {

    static PromotionResponse mapToDto(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                ProductMapper.mapToDto(promotion.getProduct()),
                promotion.getPromotionPrice(),
                promotion.getStartDate(),
                promotion.getEndDate()
        );
    }

    static Promotion mapToEntity(PromotionRequest promotionRequest) {
        return Promotion.builder()
                .promotionPrice(promotionRequest.promotionPrice())
                .startDate(promotionRequest.startDate())
                .endDate(promotionRequest.endDate())
                .build();
    }
}
