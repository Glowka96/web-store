package com.example.portfolio.webstorespring.model.dto.accounts.request;

import com.example.portfolio.webstorespring.annotations.Password;

public record UpdatePasswordRequest(
        @Password(message = "The password format is invalid") String enteredPassword,
        @Password(message = "The password format is invalid") String newPassword
) {
}
