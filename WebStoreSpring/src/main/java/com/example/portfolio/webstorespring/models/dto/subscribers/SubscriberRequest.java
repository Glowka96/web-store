package com.example.portfolio.webstorespring.models.dto.subscribers;

import jakarta.validation.constraints.Email;

public record SubscriberRequest(
        @Email
        String email) {
}
