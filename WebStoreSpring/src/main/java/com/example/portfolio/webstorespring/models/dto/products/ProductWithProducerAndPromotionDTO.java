package com.example.portfolio.webstorespring.models.dto.products;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductWithProducerAndPromotionDTO(
        Long id,
        String name,
        String imageUrl,
        Long quantity,
        String productTypeName,
        Long subcategoryId,
        BigDecimal price,
        BigDecimal promotionPrice,
        BigDecimal lowestPrice,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime endDate,
        String description,
        String producerName) implements Serializable {
}
