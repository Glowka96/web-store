package com.example.portfolio.webstorespring.buildhelpers.subscribers;


import com.example.portfolio.webstorespring.models.entities.products.Product;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscription;

import java.util.HashSet;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.BASIC_PRODUCT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

public class ProductSubscriptionBuilderHelper {

    public static ProductSubscription createProductSubscription() {
        return ProductSubscription.builder()
                .id(1L)
                .product(make(a(BASIC_PRODUCT)))
                .productSubscribers(new HashSet<>())
                .build();
    }

    public static ProductSubscription createProductSubscription(Product product) {
        return ProductSubscription.builder()
                .product(product)
                .productSubscribers(new HashSet<>())
                .build();
    }
}
