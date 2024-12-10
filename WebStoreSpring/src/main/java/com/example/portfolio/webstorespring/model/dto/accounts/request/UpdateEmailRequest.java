package com.example.portfolio.webstorespring.model.dto.accounts.request;

import jakarta.validation.constraints.Email;

public record UpdateEmailRequest(
        @Email
        String email,
        LoginRequest loginRequest
) {
}
