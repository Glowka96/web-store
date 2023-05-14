package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.annotations.Password;
import com.example.porfolio.webstorespring.annotations.UniqueEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountRequest {

    private String firstName;

    private String lastName;

    @UniqueEmail
    private String email;

    @Password
    private String password;

    @JsonProperty("address")
    private AccountAddressDto addressDto;

    private String imageUrl;
}
