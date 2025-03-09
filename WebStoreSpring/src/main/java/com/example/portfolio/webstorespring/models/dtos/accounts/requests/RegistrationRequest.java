package com.example.portfolio.webstorespring.models.dtos.accounts.requests;

import com.example.portfolio.webstorespring.annotations.Password;
import com.example.portfolio.webstorespring.annotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "The firstname can't be blank")
        @Size(min = 3, max = 20, message = "The name must between min 3 and max 20 letters")
        String firstName,

        @NotBlank(message = "The lastname can't be blank")
        @Size(min = 3, max = 20, message = "The name must between min 3 and max 20 letters")
        String lastName,

        @UniqueEmail
        @NotBlank(message = "The email can't be blank")
        @Email(message = "The login should be a valid e-mail address format")
        String email,

        @Password(message = "The password format is invalid")
        String password
) {
}
