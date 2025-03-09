package com.example.portfolio.webstorespring.controllers.subscribers;

import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.subscribers.NewsletterMessageRequest;
import com.example.portfolio.webstorespring.repositories.subscribers.NewsletterMessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class NewsletterMessageControllerIT extends AbstractAuthControllerIT {

    @Autowired
    private NewsletterMessageRepository repository;

    @AfterEach
    void deleteTestData() {
        repository.deleteAll();
    }

    @Test
    void shouldSave_forAuthenticatedAdmin_thenStatusIsCreated() {
        NewsletterMessageRequest request = new NewsletterMessageRequest(
                "Test message",
                null
        );
        HttpEntity<NewsletterMessageRequest> httpEntity = new HttpEntity<>(request, getHttpHeadersWithAdminToken());

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostAdminUri + "/newsletters/messages",
                HttpMethod.POST,
                httpEntity,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Newsletter message send to enabled subscribers", response.getBody().message());
    }

    @Test
    void shouldNotSave_forAuthenticatedUser_thenStatusIsForbidden() {
        NewsletterMessageRequest request = new NewsletterMessageRequest(
                "Test message",
                null
        );
        HttpEntity<NewsletterMessageRequest> httpEntity = new HttpEntity<>(request, getHttpHeaderWithUserToken());

        ResponseEntity<ResponseMessageDTO> response = restTemplate.exchange(
                localhostAdminUri + "/newsletters/messages",
                HttpMethod.POST,
                httpEntity,
                ResponseMessageDTO.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());

    }
}