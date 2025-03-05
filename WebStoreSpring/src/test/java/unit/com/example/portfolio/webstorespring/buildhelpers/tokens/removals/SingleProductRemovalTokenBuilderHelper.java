package com.example.portfolio.webstorespring.buildhelpers.tokens.removals;

import com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.SingleProductRemovalToken;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;

public class SingleProductRemovalTokenBuilderHelper {

    public static SingleProductRemovalToken createSingleProductRemovalTokenWithEnabledSubscriber() {
        return SingleProductRemovalToken.builder()
                .id(1L)
                .token("token123")
                .productId(1L)
                .subscriber(make(a(ProductSubscriberBuilderHelper.BASIC_PRODUCT_SUBSCRIBER)))
                .build();
    }
}
