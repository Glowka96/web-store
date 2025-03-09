package com.example.portfolio.webstorespring.models.dtos.accounts.requests;

import com.example.portfolio.webstorespring.models.dtos.AddressRegex;
import jakarta.validation.constraints.Pattern;


public record AccountAddressRequest(
        @Pattern(regexp = AddressRegex.CITY,
                message = "The city format is invalid")
        String city,

        @Pattern(regexp = AddressRegex.POSTCODE,
                message = "The postcode format is invalid")
        String postcode,

        @Pattern(regexp = AddressRegex.STREET,
                message = "The street format is invalid")
        String street) {
}
