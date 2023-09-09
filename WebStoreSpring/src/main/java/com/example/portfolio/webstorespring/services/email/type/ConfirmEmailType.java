package com.example.portfolio.webstorespring.services.email.type;

import lombok.Getter;

@Getter
public enum ConfirmEmailType implements EmailTypeStrategy {

    REGISTRATION();

    private final String title;
    private final String link;
    private final String message;

    ConfirmEmailType() {
        this.title = "Confirm email address";
        this.link = "To confirm your account, please click here: \n" +
                    "https://app-lazlbfo5va-lm.a.run.app/registration/confirm?token=";
        this.message = "Verify email by the link sent on your email address";
    }
}
