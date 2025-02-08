package com.example.portfolio.webstorespring.buildhelpers.subscribers;


import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.BASIC_PRODUCT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

public class ProductSubscriptionBuilderHelper {

    public static ProductSubscription createProductSubscription() {
        return ProductSubscription.builder()
                .id(1L)
                .product(make(a(BASIC_PRODUCT)))
                .build();
    }
}
