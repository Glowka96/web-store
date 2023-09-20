package com.example.portfolio.webstorespring.services.email.type;

import lombok.Getter;

@Getter
public enum ResetPasswordType implements EmailTypeStrategy {

    PASSWORD();


    private final String title;
    private final String emailMessage;
    private final String informationMessage;

    ResetPasswordType() {
        this.title = "Complete reset password!";
        this.emailMessage = "To reset your password, please click here: \n";
        this.informationMessage = "Sent reset password link on your email address";
    }
}
