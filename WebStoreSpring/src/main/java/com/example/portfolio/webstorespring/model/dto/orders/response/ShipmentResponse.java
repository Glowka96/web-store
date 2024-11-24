package com.example.portfolio.webstorespring.model.dto.orders.response;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;

import java.math.BigDecimal;

public record ShipmentResponse(Long id,

                               ProductWithPromotionDTO product,

                               Integer quantity,

                               BigDecimal price) {
}
