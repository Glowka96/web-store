package com.example.portfolio.webstorespring.models.dtos.products;

import java.io.Serializable;
import java.util.List;

public record PageProductsWithPromotionDTO(
        Long totalElements,
        Integer totalPages,
        List<String> sortOptions,
        List<ProductWithPromotionDTO> products) implements Serializable {
}
