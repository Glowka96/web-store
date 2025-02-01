package com.example.portfolio.webstorespring.controllers.subscribers;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.services.emails.RegisterNewsletterSubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/newsletter/registrations")
@RequiredArgsConstructor
public class RegistrationNewsletterSubscriberController {

    private final RegisterNewsletterSubscriberService registerNewsletterSubscriberService;

    @GetMapping(value = "/confirm", params = "token")
    public ResponseMessageDTO confirm(@RequestParam("token") String token) {
        return registerNewsletterSubscriberService.confirm(token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessageDTO registration(@Valid @RequestBody SubscriberRequest request) {
        return registerNewsletterSubscriberService.register(request);
    }
}
