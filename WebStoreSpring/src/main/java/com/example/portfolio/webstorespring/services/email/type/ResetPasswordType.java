package com.example.portfolio.webstorespring.services.email.type;

import lombok.Getter;

@Getter
public enum ResetPasswordType implements EmailTypeStrategy {

    PASSWORD("Complete Registration!",
            "To reset your password, please click here:  ",
            "Sent reset password link on your email address");

    private final String title;
    private final String link;
    private final String message;

    ResetPasswordType(String title, String link, String message) {
        this.title = title;
        this.link = link;
        this.message = message;
    }
}
