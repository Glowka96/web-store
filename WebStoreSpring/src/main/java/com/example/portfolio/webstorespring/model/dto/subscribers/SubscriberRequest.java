package com.example.portfolio.webstorespring.model.dto.subscribers;

import jakarta.validation.constraints.Email;

public record SubscriberRequest(
        @Email
        String email) {
}
