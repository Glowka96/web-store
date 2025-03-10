package com.example.portfolio.webstorespring.models.dtos.orders.responses;

import com.example.portfolio.webstorespring.models.dtos.products.ProductWithPromotionDTO;

import java.math.BigDecimal;

public record ShipmentResponse(Long id,

                               ProductWithPromotionDTO product,

                               Integer quantity,

                               BigDecimal price) {
}
