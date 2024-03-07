package com.example.portfolio.webstorespring.model.dto.accounts.request;

import com.example.portfolio.webstorespring.annotations.Password;
import com.example.portfolio.webstorespring.annotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "The firstname can't be blank")
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String firstName;

    @NotBlank(message = "The lastname can't be blank")
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String lastName;

    @UniqueEmail
    @NotBlank(message = "The email can't be blank")
    @Email(message = "The login should be a valid e-mail address format")
    private String email;

    @Password(message = "The password format is invalid")
    private String password;
}
