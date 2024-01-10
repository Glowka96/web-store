package com.example.portfolio.webstorespring.buildhelpers;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.enums.SortDirectionType;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsWithPromotionDTO;
import com.example.portfolio.webstorespring.model.dto.products.ProductWithPromotionDTO;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.example.portfolio.webstorespring.buildhelpers.ProductWithPromotionDtoBuildHelper.createProductWithPromotionDTO;

public class PageProductWithPromotionDTOBuilderHelper {

    public static PageProductsWithPromotionDTO createPageProductsWithPromotionDTO() {
        ProductWithPromotionDTO productWithPromotionDTO = createProductWithPromotionDTO();

        List<String> sortByList = getEnumTypes(SortByType.class);
        List<String> sortDirectionList = getEnumTypes(SortDirectionType.class);
        return new PageProductsWithPromotionDTO(
                1L,
                1,
                sortByList,
                sortDirectionList,
                List.of(productWithPromotionDTO, productWithPromotionDTO));
    }

    @NotNull
    private static <E extends Enum<E>> List<String> getEnumTypes(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map((sortByType -> sortByType.name().toLowerCase()))
                .toList();
    }
}
