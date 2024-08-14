package com.example.portfolio.webstorespring.buildhelpers.products;

import com.example.portfolio.webstorespring.model.dto.products.PageProductsOptions;

public class PageProductsOptionsBuilderHelper {

    public static PageProductsOptions createBasePageProductsOptions() {
        return new PageProductsOptions(0, 12, "name - asc");
    }
}
