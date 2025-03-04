package com.example.portfolio.webstorespring.controllers.unsubscribers;

import com.example.portfolio.webstorespring.controllers.AbstractTestRestTemplateIT;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entity.subscribers.NewsletterSubscriber;
import com.example.portfolio.webstorespring.models.entity.tokens.removals.NewsletterRemovalToken;
import com.example.portfolio.webstorespring.repositories.subscribers.NewsletterSubscriberRepository;
import com.example.portfolio.webstorespring.repositories.tokens.removals.NewsletterRemovalTokenRepository;
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

import static com.example.portfolio.webstorespring.buildhelpers.subscribers.NewsletterSubscriberBuilderHelper.createEnabledNewsletterSubscriber;
import static org.junit.jupiter.api.Assertions.*;

class UnsubscribeNewsletterControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private NewsletterSubscriberRepository subscriberRepository;
    @Autowired
    private NewsletterRemovalTokenRepository removalTokenRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private static final String URI = "/unsubscribe-newsletters/confirm";
    private Long subscriberId;

    @BeforeEach
    void init() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            NewsletterSubscriber subscriber = createEnabledNewsletterSubscriber();
            subscriber = subscriberRepository.save(subscriber);
            subscriberId = subscriber.getId();

            NewsletterRemovalToken removalToken = NewsletterRemovalToken.builder()
                    .token("token123")
                    .subscriber(subscriber)
                    .build();
            removalTokenRepository.save(removalToken);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @AfterEach
    void delete() {
        removalTokenRepository.deleteAll();
        subscriberRepository.deleteAll();
    }

    @Test
    void shouldUnsubscribeNewsletterSubscriber_thenStatusIsOk() {
        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                    localhostUri + URI + "?token=token123",
                    HttpMethod.DELETE,
                    null,
                    ResponseMessageDTO.class
            );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Your subscription was removed", response.getBody().message());

        assertFalse(removalTokenRepository.findById(subscriberId).isPresent());
    }
}