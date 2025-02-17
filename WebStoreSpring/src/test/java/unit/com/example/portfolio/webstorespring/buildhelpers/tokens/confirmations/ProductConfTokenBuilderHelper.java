package com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.TokenDetails;

public class ProductConfTokenBuilderHelper {

    public static ProductConfToken createProductConfToken(ProductSubscriber subscriber, TokenDetails tokenDetails) {
        return ProductConfToken.builder()
                .subscriber(subscriber)
                .tokenDetails(tokenDetails)
                .build();
    }
}
