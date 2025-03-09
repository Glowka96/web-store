package com.example.portfolio.webstorespring.mappers;

import com.example.portfolio.webstorespring.models.dtos.products.requests.PromotionRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.PromotionResponse;
import com.example.portfolio.webstorespring.models.entities.products.Promotion;

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
