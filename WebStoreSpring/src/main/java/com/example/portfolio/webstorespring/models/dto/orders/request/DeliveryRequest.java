package com.example.portfolio.webstorespring.models.dto.orders.request;

import com.example.portfolio.webstorespring.models.dto.AddressRegex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record DeliveryRequest(

        @Pattern(regexp = AddressRegex.CITY,
                message = "The city format is invalid")
        String city,

        @Pattern(regexp = AddressRegex.POSTCODE,
                message = "The postcode format is invalid")
        String postcode,

        @Pattern(regexp = AddressRegex.STREET,
                message = "The street format is invalid")
        String street,

        @Positive(message = "The delivery type id must be positive number")
        @NotNull(message = "The delivery type id can't be null")
        Long deliveryTypeId
) {
}
