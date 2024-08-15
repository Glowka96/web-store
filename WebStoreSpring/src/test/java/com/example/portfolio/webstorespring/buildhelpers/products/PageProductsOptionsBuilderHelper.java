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

    public static PageProductsOptions createPageProductsOptionsWithBadSortType() {
        return new PageProductsOptions(0, 12, "bad - asc");
    }

    public static PageProductsOptions createPageProductsOptionsWithBadSortDirection() {
        return new PageProductsOptions(0, 12, "name - bad");
    }

    public static Integer getNumberOfSortOptions() {
        return Arrays.stream(SortByType.values())
                .flatMap(s -> Arrays.stream(SortDirectionType.values())
                        .map(d -> s.getFieldName() + " - " + d.name().toLowerCase()))
                .toList().size();
    }

    public static List<String> getSortOptions() {
        return Arrays.stream(SortByType.values())
                .flatMap(s-> Arrays.stream(SortDirectionType.values())
                        .map(d -> s.getFieldName() + " - " + d.name().toLowerCase()))
                .toList();
    }
}
