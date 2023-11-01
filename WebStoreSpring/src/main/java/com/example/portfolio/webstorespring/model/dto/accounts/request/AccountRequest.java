package com.example.portfolio.webstorespring.model.dto.accounts.request;

import com.example.portfolio.webstorespring.annotations.Password;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
