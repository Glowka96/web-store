package com.example.portfolio.webstorespring.buildhelpers.tokens.removals;

import com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.ProductRemovalToken;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

public class ProductRemovalTokenBuilderHelper {

    public static ProductRemovalToken createNewsletterRemovalTokenWithEnabledSubscriber() {
        return ProductRemovalToken.builder()
                .id(1L)
                .token("token123")
                .subscriber(make(a(ProductSubscriberBuilderHelper.BASIC_PRODUCT_SUBSCRIBER)))
                .build();
    }
}
