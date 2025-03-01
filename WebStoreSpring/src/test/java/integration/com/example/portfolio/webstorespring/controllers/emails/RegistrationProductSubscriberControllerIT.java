package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.ProductSubscriberRequest;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.models.entity.products.Product;
import com.example.portfolio.webstorespring.models.entity.subscribers.OwnerConfToken;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscriber;
import com.example.portfolio.webstorespring.models.entity.subscribers.ProductSubscription;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ProductConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.products.ProductRepository;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriberRepository;
import com.example.portfolio.webstorespring.repositories.subscribers.ProductSubscriptionRepository;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.ProductConfTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.removals.ProductRemovalTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.removals.SingleProductRemovalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProductBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper.CREATED_AT;
import static com.example.portfolio.webstorespring.buildhelpers.subscribers.ProductSubscriberBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.ProductConfTokenBuilderHelper.createProductConfToken;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationProductSubscriberControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private ProductSubscriberRepository subscriberRepository;
    @Autowired
    private ProductConfTokenRepository confTokenRepository;
    @Autowired
    private ProductSubscriptionRepository subscriptionRepository;
    @Autowired
    private SingleProductRemovalRepository singleProductRemovalRepository;
    @Autowired
    private ProductRemovalTokenRepository removalTokenRepository;
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void delete() {
        confTokenRepository.deleteAll();
        singleProductRemovalRepository.deleteAll();
        removalTokenRepository.deleteAll();
        subscriptionRepository.deleteAll();
        subscriberRepository.deleteAll();
    }

    @Test
    void shouldConfirmProductSubscription_thenStatusIsOK() {
        ProductSubscriber subscriber = make(a(BASIC_PRODUCT_SUBSCRIBER)
                .but(withNull(ProductSubscriberBuilderHelper.ID))
                .but(with(ENABLED, Boolean.FALSE))
                .but(withNull(CREATED_AT))
        );
        subscriberRepository.save(subscriber);

        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS)
                .but(withNull(TokenDetailsBuilderHelper.ID))
                .but(with(TOKEN_NAME, "test_example"))
                .but(with(TokenDetailsBuilderHelper.CREATED_AT, LocalDateTime.now().minusMinutes(1)))
                .but(withNull(CONFIRMED_AT))
                .but(with(EXPIRES_AT, LocalDateTime.now().plusMinutes(15)))
        );

        ProductConfToken productConfToken = createProductConfToken(subscriber, tokenDetails);
        confTokenRepository.save(productConfToken);

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostUri + "/product-subscription/registrations/confirm?token=test_example",
                HttpMethod.GET,
                null,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);

        ConfToken confToken = confTokenRepository.findByTokenDetails_Token("test_example").get();
        assertNotNull(confToken.getTokenDetails().getConfirmedAt());

        OwnerConfToken ownerConfToken = subscriberRepository.findByEmail("test@test.pl").get();
        assertTrue(ownerConfToken.getEnabled());

        assertEquals(String.format("%s confirmed.", ownerConfToken.getName()), response.getBody().message());
    }

    @Test
    void shouldRegistrationProductSubscriber_thenStatusIsCreated() {
        Product product = make(a(BASIC_PRODUCT)
                .but(withNull(ProductBuilderHelper.ID))
                .but(withNull(SUBCATEGORY))
                .but(withNull(PRODUCER))
                .but(withNull(PRODUCT_TYPE))
                .but(withNull(PRICE_PROMOTIONS))
        );
        productRepository.save(product);

        String email = "test@test.pl";
        ProductSubscriberRequest request = new ProductSubscriberRequest(
                new SubscriberRequest(email),
                product.getId()
        );
        HttpEntity<ProductSubscriberRequest> httpEntity = new HttpEntity<>(request);

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostUri + "/product-subscription/registrations",
                HttpMethod.POST,
                httpEntity,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response);
        assertEquals("You have successfully subscribed to this product.", response.getBody().message());

        assertEquals(1L, singleProductRemovalRepository.findAll().size());
        assertEquals(1L, removalTokenRepository.findAll().size());
        Optional<ProductSubscription> subscription = subscriptionRepository.findByIdAndSubscriberEmail(product.getId(), email);
        assertTrue(subscription.isPresent());
    }
}