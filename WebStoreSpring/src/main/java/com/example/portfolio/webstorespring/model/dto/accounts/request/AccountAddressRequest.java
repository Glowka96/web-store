package com.example.portfolio.webstorespring.model.dto.accounts.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountAddressRequest {

    @NotBlank(message = "The city can't be blank")
    @Size(min = 2, max = 32, message = "The city must between min 3 and max 32 letters")
    private String city;

    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "The postcode format is invalid")
    private String postcode;

    @Pattern(regexp = "^((ul\\.?\\s)?([A-Z]?[a-z]{3,}(-[A-Z]?[a-z]{3,})?)\\s(\\d{1,3})[a-z]?((/|\\sm.?\\s)\\d{1,3}))$",
            message = "The street format is invalid")
    private String street;
}
