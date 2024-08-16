package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.enums.SortDirectionType;
import com.example.portfolio.webstorespring.model.dto.products.PageProductsOptions;

import java.util.Arrays;
import java.util.List;

public class PageProductsOptionsBuilderHelper {

    public static PageProductsOptions createBasePageProductsOptions() {
        return new PageProductsOptions(0, 12, "name - asc");
    }

    public static PageProductsOptions createBasePageProductsOptions(String sortType, String sortDirection) {
        return new PageProductsOptions(0, 12, sortType + " - " + sortDirection);
    }

    public static List<String> getSortOptions() {
        return Arrays.stream(SortByType.values())
                .flatMap(s-> Arrays.stream(SortDirectionType.values())
                        .map(d -> s.getFieldName() + " - " + d.name().toLowerCase()))
                .toList();
    }
    public static Integer getNumberOfSortOptions() {
        return getSortOptions().size();
    }
}
