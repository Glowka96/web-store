package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.annotations.Password;
import com.example.porfolio.webstorespring.annotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotNull(message = "The firstname can't be null")
    @NotBlank(message = "The firstname can't be blank")
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String firstName;
    @NotNull(message = "The lastname can't be null")
    @NotBlank(message = "The lastname can't be blank")
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String lastName;
    @UniqueEmail
    @NotNull(message = "The email can't be null")
    @NotBlank(message = "The email can't be blank")
    @Email(message = "Login should be a valid e-mail address format")
    private String email;
    @Password
    @NotNull(message = "The email can't be null")
    @NotBlank(message = "The email can't be blank")
    private String password;


}
