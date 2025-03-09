package com.example.portfolio.webstorespring.models.dtos.subscribers;

import jakarta.validation.constraints.Email;

public record SubscriberRequest(
        @Email
        String email) {
}
