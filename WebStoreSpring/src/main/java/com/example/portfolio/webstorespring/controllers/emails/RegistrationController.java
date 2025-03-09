package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.services.emails.registrations.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessageDTO registration(@Valid @RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @PatchMapping(value = "/confirm", params = "token")
    public ResponseMessageDTO confirm(@RequestParam("token") String token) {
        return registrationService.confirm(token);
    }
}
