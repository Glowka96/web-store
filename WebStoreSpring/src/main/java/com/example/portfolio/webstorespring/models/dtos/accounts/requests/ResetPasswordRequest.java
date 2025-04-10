package com.example.portfolio.webstorespring.models.dtos.accounts.requests;

import com.example.portfolio.webstorespring.annotations.Password;

public record ResetPasswordRequest(
        @Password(message = "The password format is invalid") String password) {
}
