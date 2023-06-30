package com.example.porfolio.webstorespring.model.dto.accounts;

import com.example.porfolio.webstorespring.annotations.Password;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountRequest {

    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String firstName;

    @Size(min=3, max = 20, message = "The name must between min 3 and max 20 letters")
    private String lastName;

    @Password(message = "The password format is invalid")
    private String password;

    @Pattern(regexp = "^(http(s)?:)?([\\/|\\.|\\w|\\s|-])*\\.(?:jpg|png)$",
            message = "This image url format is invalid")
    private String imageUrl;
}
