package com.example.portfolio.webstorespring.model.dto.products;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductWithProducerAndPromotionDTO(
        Long id,
        String name,
        String imageUrl,
        Long quantity,
        String productTypeName,
        BigDecimal price,
        BigDecimal promotionPrice,
        BigDecimal lowestPrice,
        LocalDateTime endDate,
        String description,
        String producerName) implements Serializable {
}
