package com.example.portfolio.webstorespring.initTestData;

import com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper;
import com.example.portfolio.webstorespring.controllers.InitTestData;
import com.example.portfolio.webstorespring.models.entity.products.Product;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriberRepository;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriptionRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.ID;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriptionBuilderHelper.createProductSubscription;
import static com.natpryce.makeiteasy.MakeItEasy.*;

public class InitSubscriptionData implements InitTestData {

    @Autowired
    private ProductSubscriptionRepository subscriptionRepository;
    @Autowired
    public ProductSubscriberRepository subscriberRepository;
    @Autowired
    private ProductRepository productRepository;

    @Getter
    private Long productId;

    @Transactional
    public void initTestData() {
        ProductSubscriber enabledSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER)
                .but(withNull(ProductSubscriberBuilderHelper.ID)));
        ProductSubscriber enabledSubscriber2 = make(a(BASIC_PRODUCT_SUBSCRIBER)
                .but(withNull(ProductSubscriberBuilderHelper.ID))
                .but(with(EMAIL, "enabled2@test.pl")));
        ProductSubscriber disabledSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER)
                .but(withNull(ProductSubscriberBuilderHelper.ID))
                .but(with(EMAIL, "disabled@test.pl"))
                .but(with(ENABLED, Boolean.FALSE)));
        enabledSubscriber = subscriberRepository.save(enabledSubscriber);
        enabledSubscriber2 = subscriberRepository.save(enabledSubscriber2);
        disabledSubscriber = subscriberRepository.save(disabledSubscriber);

        Product product = make(a(BASIC_PRODUCT)
                .but(withNull(ID))
                .but(withNull(SUBCATEGORY))
                .but(withNull(PRODUCT_TYPE))
                .but(withNull(PRODUCER))
                .but(withNull(PRICE_PROMOTIONS))
                .but(with(QUANTITY, 0L))
        );
        product = productRepository.save(product);
        productId = product.getId();

        ProductSubscription subscription = createProductSubscription(product);
        subscription.addSubscriber(enabledSubscriber);
        subscription.addSubscriber(enabledSubscriber2);
        subscription.addSubscriber(disabledSubscriber);

        subscriptionRepository.save(subscription);
    }

    public void deleteTestData() {
        subscriptionRepository.deleteAll();
        subscriberRepository.deleteAll();
        productRepository.deleteAll();
    }
}
