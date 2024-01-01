package com.example.portfolio.webstorespring.model.dto.accounts.request;

import com.example.portfolio.webstorespring.annotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "The email can't be blank")
    @Email(message = "The login should be a valid e-mail address format")
    private String email;

    @Password(message = "The password format is invalid")
    private String password;
}
