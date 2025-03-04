package com.example.portfolio.webstorespring.controllers.subscribers;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.NewsletterMessageRequest;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/newsletters/messages")
@RequiredArgsConstructor
public class NewsletterMessageController {

    private final NewsletterMessageService newsletterMessageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessageDTO save(NewsletterMessageRequest request) {
        return newsletterMessageService.save(request);
    }
}

