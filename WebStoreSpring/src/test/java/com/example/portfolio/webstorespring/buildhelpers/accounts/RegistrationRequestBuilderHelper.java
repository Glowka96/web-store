package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;

public class RegistrationRequestBuilderHelper {

    public static RegistrationRequest createRegistrationRequest() {
        return RegistrationRequest.builder()
                .firstName("Name")
                .lastName("Lastname")
                .email("test@test.pl")
                .password("Password123*")
                .build();
    }
}
