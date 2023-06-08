package com.example.porfolio.webstorespring.model.dto.accounts;

import lombok.Data;

@Data
public class AccountAddressResponse {

    private Long id;

    private String city;

    private String postcode;

    private String street;
}
