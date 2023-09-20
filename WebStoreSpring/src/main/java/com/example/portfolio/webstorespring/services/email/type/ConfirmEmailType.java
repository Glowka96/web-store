package com.example.portfolio.webstorespring.services.email.type;

import lombok.Getter;

@Getter
public enum ConfirmEmailType implements EmailTypeStrategy {

    REGISTRATION();

    private final String title;
    private final String emailMessage;
    private final String informationMessage;

    ConfirmEmailType() {
        this.title = "Confirm email address";
        this.emailMessage = "To confirm your account, please click here: \n";
        this.informationMessage = "Verify email by the link sent on your email address";
    }
}
