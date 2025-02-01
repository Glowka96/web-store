package com.example.portfolio.webstorespring.models.dto.accounts.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountRequest(
        @NotBlank(message = "The first name can't be blank")
        @Size(min = 3, max = 20, message = "The name must between min 3 and max 20 letters")
        String firstName,

        @NotBlank(message = "The last name can't be blank")
        @Size(min = 3, max = 20, message = "The name must between min 3 and max 20 letters")
        String lastName,

        @Pattern(regexp = "^(https?://)?[\\w./\\s-]*\\.(?:jpg|png)$",
                message = "This image url format is invalid")
        String imageUrl) {
}

