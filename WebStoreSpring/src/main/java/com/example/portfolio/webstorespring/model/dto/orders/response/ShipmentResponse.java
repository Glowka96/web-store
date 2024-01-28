package com.example.portfolio.webstorespring.model.dto.orders.response;

import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {

    private Long id;

    private ProductWithPromotionDTO product;

    private Integer quantity;

    private BigDecimal price;
}
