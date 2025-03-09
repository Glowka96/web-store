package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.configs.ContainersConfig;
import com.example.portfolio.webstorespring.models.entities.products.Product;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper.BASIC_PRODUCT_SUBSCRIBER;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriptionBuilderHelper.createProductSubscription;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.*;

@Import({ContainersConfig.class})
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductSubscriberRepositoryIT {

    @Autowired
    private ProductSubscriberRepository subscriberRepository;
    @Autowired
    private ProductSubscriptionRepository subscriptionRepository;
    @Autowired
    private ProductRepository productRepository;

    private static Long id;

    @BeforeEach
    void init() {
        ProductSubscriber productSubscriber = make(a(BASIC_PRODUCT_SUBSCRIBER));
        productSubscriber = subscriberRepository.save(productSubscriber);
        id = productSubscriber.getId();

        Product product1 = getMakeOfProduct("Test1");
        Product product2 = getMakeOfProduct("Test2");
        productRepository.save(product1);
        productRepository.save(product2);

        ProductSubscription productSubscription1 = createProductSubscription(product1);
        ProductSubscription productSubscription2 = createProductSubscription(product2);

        productSubscription1.addSubscriber(productSubscriber);
        productSubscription2.addSubscriber(productSubscriber);

        subscriptionRepository.save(productSubscription1);
        subscriptionRepository.save(productSubscription2);
    }

    @Test
    void shouldFindWithSubscriptionById() {
        Optional<ProductSubscriber> optionalProductSubscriber = subscriberRepository.findWithSubscriptionById(id);

        assertTrue(optionalProductSubscriber.isPresent());
        assertFalse(optionalProductSubscriber.get().getSubscriptions().isEmpty());
        assertEquals(2L, optionalProductSubscriber.get().getSubscriptions().size());
    }

    private static Product getMakeOfProduct(String name) {
        return make(a(BASIC_PRODUCT)
                .but(withNull(ID))
                .but(with(NAME, name))
                .but(withNull(SUBCATEGORY))
                .but(withNull(PRODUCT_TYPE))
                .but(withNull(PRODUCER))
                .but(withNull(PRICE_PROMOTIONS))
        );
    }
}