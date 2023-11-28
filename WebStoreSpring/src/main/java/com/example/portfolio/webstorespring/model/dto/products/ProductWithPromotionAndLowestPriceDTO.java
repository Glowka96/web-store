package com.example.portfolio.webstorespring.model.dto.products;

import com.example.portfolio.webstorespring.enums.ProductType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public record ProductWithPromotionAndLowestPriceDTO(
        Long id,
        String name,
        String imageUrl,
        Long quantity,
        ProductType productType,
        BigDecimal price,
        BigDecimal promotionPrice,
        BigDecimal lowestPrice,
        Date endDate) implements Serializable {
}
