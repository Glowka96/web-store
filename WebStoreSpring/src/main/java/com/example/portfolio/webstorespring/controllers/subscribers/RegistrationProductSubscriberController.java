package com.example.portfolio.webstorespring.controllers.subscribers;

import com.example.portfolio.webstorespring.model.dto.subscribers.ProductSubscriberRequest;
import com.example.portfolio.webstorespring.services.emails.RegisterProductSubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/product-subscription/registrations")
@RequiredArgsConstructor
public class RegistrationProductSubscriberController {

    private final RegisterProductSubscriberService service;

    @GetMapping(value = "/confirm", params = "token")
    public Map<String, Object> confirm(@RequestParam("token") String token) {
        return service.confirm(token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registration(@Valid @RequestBody ProductSubscriberRequest request) {
        return service.register(request);
    }
}
