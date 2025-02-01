package com.example.portfolio.webstorespring.models.dto.orders.request;

import com.example.portfolio.webstorespring.models.dto.AddressRegex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record DeliveryRequest(
        @Pattern(regexp = AddressRegex.COMPLETE_ADDRESS,
                message = "The delivery address format is invalid")
        String deliveryAddress,

        @Positive(message = "The delivery type id must be positive number")
        @NotNull(message = "The delivery type id can't be null")
        Long deliveryTypeId
) {
}
