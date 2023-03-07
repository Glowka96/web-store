package com.example.porfolio.webstorespring.model.dto.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AccountAddressDto {

    private Long id;

    private String city;

    private String postcode;

    private String address;
    @JsonIgnore
    private AccountDto accountDto;
}
