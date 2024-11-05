package com.example.portfolio.webstorespring.model.dto.products;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductWithPromotionDTO(
        Long id,
        String name,
        String imageUrl,
        Long quantity,
        Long subcategoryId,
        BigDecimal price,
        BigDecimal promotionPrice,
        BigDecimal lowestPrice) implements Serializable {
}
