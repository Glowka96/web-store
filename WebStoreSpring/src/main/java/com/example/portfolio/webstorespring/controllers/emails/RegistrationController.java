package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.services.email.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping(value = "/confirm", params = "token")
    public Map<String, Object> confirm(@RequestParam("token") String token) {
        return registrationService.confirm(token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registration(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }
}
