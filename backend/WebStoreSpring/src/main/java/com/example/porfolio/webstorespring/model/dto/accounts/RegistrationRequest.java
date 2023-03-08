package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.annotations.Password;
import com.example.porfolio.webstorespring.annotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RegistrationRequest {

    private String firstName;
    private String lastName;
    @UniqueEmail
    @Email(message = "Login should be a valid e-mail address format")
    private String email;
    @Password
    private String password;
}
