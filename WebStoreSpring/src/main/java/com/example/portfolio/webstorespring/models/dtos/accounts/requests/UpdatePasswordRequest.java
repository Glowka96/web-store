package com.example.portfolio.webstorespring.models.dtos.accounts.requests;

import com.example.portfolio.webstorespring.annotations.Password;

public record UpdatePasswordRequest(
        @Password(message = "The password format is invalid") String enteredPassword,
        @Password(message = "The password format is invalid") String newPassword
) {
}
