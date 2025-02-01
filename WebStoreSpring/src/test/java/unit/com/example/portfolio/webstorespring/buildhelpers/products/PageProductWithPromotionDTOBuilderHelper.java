package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.models.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.models.dto.products.ProductWithPromotionDTO;

import java.util.List;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductWithPromotionDtoBuildHelper.createProductWithPromotionDTO;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductsPageOptionsBuilderHelper.getSortOptions;

public class PageProductWithPromotionDTOBuilderHelper {

    public static PageProductsWithPromotionDTO createPageProductsWithPromotionDTO() {
        ProductWithPromotionDTO productWithPromotionDTO = createProductWithPromotionDTO();
        return new PageProductsWithPromotionDTO(
                1L,
                1,
                getSortOptions(),
                List.of(productWithPromotionDTO, productWithPromotionDTO));
    }
}
