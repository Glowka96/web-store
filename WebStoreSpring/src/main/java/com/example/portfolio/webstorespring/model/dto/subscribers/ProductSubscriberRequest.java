package com.example.portfolio.webstorespring.model.dto.subscribers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

public record ProductSubscriberRequest(
        @Valid
        SubscriberRequest subscriberRequest,

        @Positive(message = "The product id must be positive number")
        Long productId) {
}
