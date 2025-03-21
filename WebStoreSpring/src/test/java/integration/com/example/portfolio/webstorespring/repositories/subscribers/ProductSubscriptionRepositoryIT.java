package com.example.portfolio.webstorespring.repositories.subscribers;

import com.example.portfolio.webstorespring.configs.ContainersConfig;
import com.example.portfolio.webstorespring.initTestData.InitSubscriptionData;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscription;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({ContainersConfig.class, InitSubscriptionData.class})
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductSubscriptionRepositoryIT {

    @Autowired
    private InitSubscriptionData initSubscriptionData;

    @Autowired
    private ProductSubscriptionRepository subscriptionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void init() {
        initSubscriptionData.initTestData();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByIdWithEnabledSubscribers() {
        Optional<ProductSubscription> optionalProductSubscription = subscriptionRepository.findByIdWithEnabledSubscribers(initSubscriptionData.getProductId());

        assertTrue(optionalProductSubscription.isPresent());

        assertEquals(2L, optionalProductSubscription.get().getProductSubscribers().size());
        assertTrue(optionalProductSubscription.get().getProductSubscribers().stream()
                .allMatch(ProductSubscriber::getEnabled));
    }

    @Test
    void findByIdAndSubscriberEmail() {
        String testEmail = "test@test.pl";

        Optional<ProductSubscription> optionalProductSubscription = subscriptionRepository.findByIdAndSubscriberEmail(initSubscriptionData.getProductId(), testEmail);

        assertTrue(optionalProductSubscription.isPresent());

        assertEquals(1L, optionalProductSubscription.get().getProductSubscribers().size());
        assertTrue(optionalProductSubscription.get().getProductSubscribers().stream()
                .allMatch(productSubscriber -> productSubscriber.getEmail().equals(testEmail)));
    }
}