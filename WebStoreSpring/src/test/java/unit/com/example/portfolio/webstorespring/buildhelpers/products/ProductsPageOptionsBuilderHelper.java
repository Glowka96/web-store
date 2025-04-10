package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.enums.SortByType;
import com.example.portfolio.webstorespring.models.dtos.products.ProductsPageOptions;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

public class ProductsPageOptionsBuilderHelper {

    public static ProductsPageOptions createBaseProductPageOptions() {
        return new ProductsPageOptions(0, 12, "name - asc");
    }

    public static ProductsPageOptions createBaseProductPageOptions(String sortType, String sortDirection) {
        return new ProductsPageOptions(0, 12, sortType + " - " + sortDirection);
    }

    public static List<String> getSortOptions() {
        return Arrays.stream(SortByType.values())
                .flatMap(s-> Arrays.stream(Sort.Direction.values())
                        .map(d -> s.getFieldName() + " - " + d.name().toLowerCase()))
                .toList();
    }
    public static Integer getNumberOfSortOptions() {
        return getSortOptions().size();
    }
}
