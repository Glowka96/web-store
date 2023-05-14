package com.example.porfolio.webstorespring.model.dto.accounts;

import lombok.Data;

@Data
public class AccountAddressDto {

    private Long id;

    private String city;

    private String postcode;

    private String street;
}
