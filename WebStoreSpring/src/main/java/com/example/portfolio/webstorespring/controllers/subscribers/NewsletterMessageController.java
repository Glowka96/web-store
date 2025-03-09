package com.example.portfolio.webstorespring.controllers.subscribers;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.subscribers.NewsletterMessageRequest;
import com.example.portfolio.webstorespring.services.subscribers.NewsletterMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/newsletters/messages")
@RequiredArgsConstructor
public class NewsletterMessageController {

    private final NewsletterMessageService newsletterMessageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessageDTO save(@Valid @RequestBody NewsletterMessageRequest request) {
        return newsletterMessageService.save(request);
    }
}

