package com.example.portfolio.webstorespring.controllers.unsubscribers;

import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.initTestData.InitSubscriptionData;
import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entities.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entities.tokens.removals.ProductRemovalToken;
import com.example.portfolio.webstorespring.models.entities.tokens.removals.SingleProductRemovalToken;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriberRepository;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriptionRepository;
import com.example.portfolio.webstorespring.repositories.tokens.removals.ProductRemovalTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.removals.SingleProductRemovalRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UnsubscribeProductControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private ProductRemovalTokenRepository productRemovalTokenRepository;
    @Autowired
    private ProductSubscriberRepository productSubscriberRepository;
    @Autowired
    private SingleProductRemovalRepository singleProductRemovalRepository;
    @Autowired
    private ProductSubscriptionRepository productSubscriptionRepository;
    @Autowired
    private InitSubscriptionData initSubscriptionData;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private static final String URI = "/unsubscribe-product-subscriptions";

    @BeforeEach
    void init() {
        initSubscriptionData.initTestData();
    }

    @AfterEach
    void delete() {
        initSubscriptionData.deleteTestData();
        productRemovalTokenRepository.deleteAll();
        singleProductRemovalRepository.deleteAll();
    }

    @Test
    void shouldUnsubscribeAllSubscriptions_thenStatusIsOK() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            ProductRemovalToken productRemovalToken = ProductRemovalToken.builder()
                    .token("token123")
                    .subscriber(initSubscriptionData.getEnabledSubscriber())
                    .build();
            productRemovalTokenRepository.save(productRemovalToken);
            log.info("Saved token");
            transactionManager.commit(status);
            ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                    localhostUri + URI + "/confirm?token=token123",
                    HttpMethod.DELETE,
                    null,
                    ResponseMessageDTO.class
            );

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Your all subscription was removed", response.getBody().message());

            assertFalse(productSubscriberRepository.findById(
                    initSubscriptionData.getEnabledSubscriberId()
            ).isPresent());
            assertFalse(productRemovalTokenRepository.findById(
                    initSubscriptionData.getEnabledSubscriberId()
            ).isPresent());
            assertFalse(productSubscriptionRepository.findByIdAndSubscriberEmail(
                    initSubscriptionData.getProductId(), initSubscriptionData.getEnabledSubscriber().getEmail()
            ).isPresent());
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }

    @Test
    void shouldUnsubscribeSingleProduct_thenStatusIsOK() {
        SingleProductRemovalToken singleProductRemovalToken = SingleProductRemovalToken.builder()
                .token("token123")
                .productId(initSubscriptionData.getProductId())
                .subscriber(initSubscriptionData.getEnabledSubscriber())
                .build();
        singleProductRemovalRepository.save(singleProductRemovalToken);

        Integer sizeOfSubscription = null;
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Optional<ProductSubscriber> subscriber = productSubscriberRepository.findWithSubscriptionById(
                    initSubscriptionData.getEnabledSubscriberId());
            sizeOfSubscription = subscriber.get().getSubscriptions().size();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostUri + URI + "/single/confirm?token=token123",
                HttpMethod.DELETE,
                null,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Your subscription for this: Test was removed.", response.getBody().message());
        TransactionStatus status2 = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Optional<ProductSubscriber> updatedSubscriber = productSubscriberRepository.findWithSubscriptionById(
                    initSubscriptionData.getEnabledSubscriberId());
            assertTrue(updatedSubscriber.isPresent());
            assertNotEquals(sizeOfSubscription, updatedSubscriber.get().getSubscriptions().size());
            transactionManager.commit(status2);
        } catch (Exception e) {
            transactionManager.rollback(status2);
        }
        assertFalse(singleProductRemovalRepository.findByToken("token123").isPresent());
    }
}