package com.example.portfolio.webstorespring.controllers.subscribers;

import com.example.portfolio.webstorespring.controllers.AbstractAuthControllerIT;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.NewsletterMessageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NewsletterMessageControllerIT extends AbstractAuthControllerIT {

    @Test
    void shouldSave() {
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

    }
}