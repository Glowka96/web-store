package com.example.portfolio.webstorespring.model.dto.accounts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "The city can't be null")
    @NotBlank(message = "The city can't be blank")
    @Size(min = 2, max = 32, message = "The city must between min 3 and max 32 letters")
    private String city;

    @NotNull(message = "The street can't be null")
    @NotBlank(message = "The street can't be blank")
    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "The postcode format is invalid")
    private String postcode;

    @NotNull(message = "The street can't be null")
    @NotBlank(message = "The street can't be blank")
    @Pattern(regexp = "^(ul(.)?\\s)?[A-Z]?[a-z]*\\s[0-9]{1,3}(/[0-9]{1,3})|(\\sm\\.?\\s[0-9]{1,3})[a-z]?$",
            message = "The street format is invalid")
    private String street;
}
