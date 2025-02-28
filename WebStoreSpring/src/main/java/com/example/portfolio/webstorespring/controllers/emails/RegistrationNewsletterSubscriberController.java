package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.services.emails.registrations.RegisterNewsletterSubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// TODO: Think about moved these classes for emails directory or emails' classes moved to other directory

@RestController
@RequestMapping("/api/v1/newsletter/registrations")
@RequiredArgsConstructor
public class RegistrationNewsletterSubscriberController {

    private final RegisterNewsletterSubscriberService service;

    @GetMapping(value = "/confirm", params = "token")
    public ResponseMessageDTO confirm(@RequestParam("token") String token) {
        return service.confirm(token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessageDTO registration(@Valid @RequestBody SubscriberRequest request) {
        return service.register(request);
    }
}
