package com.example.portfolio.webstorespring.model.dto.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @JsonProperty("address")
    private AccountAddressRequest addressDto;

    private String imageUrl;
}
