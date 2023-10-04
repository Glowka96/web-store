package com.example.portfolio.webstorespring.model.dto.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountAddressResponse {

    private Long id;

    private String city;

    private String postcode;

    private String street;
}
