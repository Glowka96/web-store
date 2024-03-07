package com.example.portfolio.webstorespring.model.dto.products;

import java.io.Serializable;
import java.util.List;

public record PageProductsWithPromotionDTO(
        Long totalElements,
        Integer totalPages,
        List<String> sortByTypes,
        List<String> sortDirectionTypes,
        List<ProductWithPromotionDTO> products) implements Serializable {
}
