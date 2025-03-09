package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.models.dtos.accounts.requests.RegistrationRequest;

public class RegistrationRequestBuilderHelper {

    public static RegistrationRequest createRegistrationRequest() {
        return new RegistrationRequest(
                "Name",
                "Lastname",
                "test@test.pl",
                "Password123*"
        );
    }
}
