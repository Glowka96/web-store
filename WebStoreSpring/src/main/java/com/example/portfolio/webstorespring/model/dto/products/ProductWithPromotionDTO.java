package com.example.portfolio.webstorespring.model.dto.products;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public record ProductWithPromotionDTO(
        Long id,
        String name,
        String imageUrl,
        Long quantity,
        String productType,
        BigDecimal price,
        BigDecimal promotionPrice,
        BigDecimal lowestPrice,
        Date endDate) implements Serializable {
}
