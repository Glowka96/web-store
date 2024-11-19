package com.example.portfolio.webstorespring.model.dto.accounts.request;

import com.example.portfolio.webstorespring.annotations.Password;

public record ResetPasswordRequest(
        @Password(message = "The password format is invalid") String password) {
}
