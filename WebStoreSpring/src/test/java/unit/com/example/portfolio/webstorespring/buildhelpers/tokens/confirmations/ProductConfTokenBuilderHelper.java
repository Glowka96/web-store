package com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations;

import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.TokenDetails;

public class ProductConfTokenBuilderHelper {

    public static ProductConfToken createProductConfToken(ProductSubscriber subscriber, TokenDetails tokenDetails) {
        return ProductConfToken.builder()
                .subscriber(subscriber)
                .tokenDetails(tokenDetails)
                .build();
    }
}
