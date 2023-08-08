package com.example.portfolio.webstorespring.services.email.type;

import lombok.Getter;

@Getter
public enum ConfirmEmailType implements EmailTypeStrategy {

    REGISTRATION("Confirm email address",
            "To confirm your account, please click here: \n" +
                    "https://app-lazlbfo5va-lm.a.run.app/registration/confirm?token=" ,
            "Verify email by the link sent on your email address");

    private final String title;
    private final String link;
    private final String message;
    ConfirmEmailType(String title, String link, String message) {
        this.title = title;
        this.link = link;
        this.message = message;
    }
}
