package com.example.portfolio.webstorespring.models.dto.subscribers;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record NewsletterMessageRequest(
        @NotBlank(message = "The message can't be blank")
        String message,
        LocalDateTime sendDate
) {
}
