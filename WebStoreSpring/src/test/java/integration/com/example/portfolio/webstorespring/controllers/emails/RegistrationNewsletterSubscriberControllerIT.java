package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper;
import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.ConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.NewsletterConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.subscribers.NewsletterSubscriberRepository;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.NewsletterConfTokenRepository;
import com.example.portfolio.webstorespring.repositories.tokens.removals.NewsletterRemovalTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.subscribers.NewsletterSubscriberBuilderHelper.createDisabledNewsletterSubscriber;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.NewsletterConfTokenBuilderHelper.createNewsletterConfToken;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationNewsletterSubscriberControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private NewsletterSubscriberRepository subscriberRepository;
    @Autowired
    private NewsletterConfTokenRepository confTokenRepository;
    @Autowired
    private NewsletterRemovalTokenRepository removalTokenRepository;

    private static final String URI = "/newsletters/registrations";

    @AfterEach
    void delete() {
        removalTokenRepository.deleteAll();
        confTokenRepository.deleteAll();
        subscriberRepository.deleteAll();
    }

    @Test
    void shouldConfirmNewsletterSubscriber_thenStatusIsOk() {
        NewsletterSubscriber subscriber = createDisabledNewsletterSubscriber();
        subscriber = subscriberRepository.save(subscriber);

        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS)
                .but(withNull(TokenDetailsBuilderHelper.ID))
                .but(with(TOKEN_NAME, "test_example"))
                .but(with(TokenDetailsBuilderHelper.CREATED_AT, LocalDateTime.now().minusMinutes(1)))
                .but(withNull(CONFIRMED_AT))
                .but(with(EXPIRES_AT, LocalDateTime.now().plusMinutes(15)))
        );
        NewsletterConfToken newsletterConfToken = createNewsletterConfToken(subscriber, tokenDetails);
        confTokenRepository.save(newsletterConfToken);

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostUri + URI + "/confirm?token=test_example",
                HttpMethod.GET,
                null,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Newsletter subscriber confirmed.", response.getBody().message());

        ConfToken confToken = confTokenRepository.findByTokenDetails_Token("test_example").get();
        assertNotNull(confToken.getTokenDetails().getConfirmedAt());

        Optional<NewsletterSubscriber> ownerConfToken = subscriberRepository.findById(subscriber.getId());
        assertTrue(ownerConfToken.get().getEnabled());

        assertEquals(1L, removalTokenRepository.findAll().size());
    }

    @Test
    void shouldRegistrationNewsletterSubscriber_thenStatusIsCreated() {
        String email = "test@test.pl";
        SubscriberRequest request = new SubscriberRequest(email);
        HttpEntity<SubscriberRequest> httpEntity = new HttpEntity<>(request);

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostUri + URI,
                HttpMethod.POST,
                httpEntity,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Verify your email address using the link in your email.", response.getBody().message());

        assertTrue(subscriberRepository.existsByEmail(email));
    }
}