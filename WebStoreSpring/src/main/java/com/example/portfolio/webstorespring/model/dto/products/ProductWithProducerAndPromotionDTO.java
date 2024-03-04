package com.example.portfolio.webstorespring.model.dto.products;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public record ProductWithProducerAndPromotionDTO(
        Long id,
        String name,
        String imageUrl,
        Long quantity,
        BigDecimal price,
        BigDecimal promotionPrice,
        BigDecimal lowestPrice,
        Date endDate,
        String description,
        String producerName) implements Serializable {
}
