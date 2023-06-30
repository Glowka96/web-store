package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.annotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @NotNull(message = "The email can't be null")
    @NotBlank(message = "The email can't be blank")
    @Email(message = "The login should be a valid e-mail address format")
    private String email;

    @NotNull(message = "The password can't be null")
    @NotBlank(message = "The password can't be blank")
    @Password(message = "The password format is invalid")
    private String password;
}
