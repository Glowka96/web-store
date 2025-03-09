package com.example.portfolio.webstorespring.models.dtos.accounts.requests;

import jakarta.validation.constraints.Email;

public record UpdateEmailRequest(
        @Email
        String email,
        LoginRequest loginRequest
) {
}
