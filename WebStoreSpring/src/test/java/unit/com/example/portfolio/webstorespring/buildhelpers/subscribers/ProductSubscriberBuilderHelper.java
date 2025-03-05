package com.example.portfolio.webstorespring.buildhelpers.subscribers;

import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.SingleProductRemovalToken;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;

public class ProductSubscriberBuilderHelper {

    public static final Property<ProductSubscriber, Long> ID = new Property<>();
    public static final Property<ProductSubscriber, String> EMAIL = new Property<>();
    public static final Property<ProductSubscriber, Boolean> ENABLED = new Property<>();
    public static final Property<ProductSubscriber, LocalDateTime> CREATED_AT = new Property<>();
    public static final Property<ProductSubscriber, List<SingleProductRemovalToken>> TOKENS = new Property<>();
    public static final Property<ProductSubscriber, Set<ProductSubscription>> SUBSCRIPTIONS = new Property<>();

    private static final String PRODUCT_SUBSCRIBER_EMAIL = "test@test.pl";

    public static final Instantiator<ProductSubscriber> BASIC_PRODUCT_SUBSCRIBER = lookup ->
            ProductSubscriber.builder()
                    .id(lookup.valueOf(ID, 1L))
                    .email(lookup.valueOf(EMAIL, PRODUCT_SUBSCRIBER_EMAIL))
                    .enabled(lookup.valueOf(ENABLED, Boolean.TRUE))
                    .createdAt(lookup.valueOf(CREATED_AT, LOCAL_DATE_TIME))
                    .tokens(lookup.valueOf(TOKENS, new ArrayList<>()))
                    .subscriptions(lookup.valueOf(SUBSCRIPTIONS, new HashSet<>()))
                    .build();
}
