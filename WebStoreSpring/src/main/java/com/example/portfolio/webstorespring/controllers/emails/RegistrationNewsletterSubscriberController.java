package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.subscribers.SubscriberRequest;
import com.example.portfolio.webstorespring.services.emails.registrations.RegisterNewsletterSubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/newsletters/registrations")
@RequiredArgsConstructor
public class RegistrationNewsletterSubscriberController {

    private final RegisterNewsletterSubscriberService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessageDTO registration(@Valid @RequestBody SubscriberRequest request) {
        return service.register(request);
    }

    @PatchMapping(value = "/confirm", params = "token")
    public ResponseMessageDTO confirm(@RequestParam("token") String token) {
        return service.confirm(token);
    }

}
