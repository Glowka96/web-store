package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.annotations.Password;
import com.example.porfolio.webstorespring.annotations.UniqueEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountRequest {

    @NotNull(message = "The firstname can't be null")
    @NotBlank(message = "The firstname can't be blank")
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String firstName;

    @NotNull(message = "The lastname can't be null")
    @NotBlank(message = "The lastname can't be blank")
    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String lastName;

    @Password
    @NotNull(message = "The password can't be null")
    @NotBlank(message = "The password can't be blank")
    private String password;

    private String imageUrl;
}
