package com.example.portfolio.webstorespring.models.dto.subscribers;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record NewsletterMessageRequest(
        @NotBlank(message = "The message can't be blank")
        String message,
        @Future(message = "The send date must be in the future")
        LocalDateTime sendDate
) {
}
