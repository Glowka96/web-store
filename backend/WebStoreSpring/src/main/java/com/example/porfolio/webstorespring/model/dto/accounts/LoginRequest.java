package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.annotations.Password;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "Login should be a valid e-mail address format")
    private String email;

    @Password
    private String password;
}
