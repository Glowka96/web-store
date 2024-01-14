package com.example.portfolio.webstorespring.model.dto.accounts.request;

import com.example.portfolio.webstorespring.model.dto.AddressRegex;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountAddressRequest {

    @Pattern(regexp = "^" + AddressRegex.CITY + "$",
            message = "The city format is invalid")
    private String city;

    @Pattern(regexp = "^" +AddressRegex.POSTCODE  + "$",
            message = "The postcode format is invalid")
    private String postcode;

    @Pattern(regexp = "^" + AddressRegex.STREET + "$",
            message = "The street format is invalid")
    private String street;
}
