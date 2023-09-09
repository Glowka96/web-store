package com.example.portfolio.webstorespring.services.email.type;

import lombok.Getter;

@Getter
public enum ResetPasswordType implements EmailTypeStrategy {

    PASSWORD();

    private final String title;
    private final String link;
    private final String message;

    ResetPasswordType() {
        this.title = "Complete reset password!";
        this.link = "To reset your password, please click here:  ";
        this.message = "Sent reset password link on your email address";
    }
}
