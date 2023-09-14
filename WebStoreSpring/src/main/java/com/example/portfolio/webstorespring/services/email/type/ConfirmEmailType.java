package com.example.portfolio.webstorespring.services.email.type;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public enum ConfirmEmailType implements EmailTypeStrategy {

    REGISTRATION();

    @Value("${email.confirmation.link}")
    private String confirmLink;

    private final String title;
    private final String emailMessageWithLink;
    private final String informationMessage;

    ConfirmEmailType() {
        this.title = "Confirm email address";
        this.emailMessageWithLink = "To confirm your account, please click here: \n" + confirmLink;
        this.informationMessage = "Verify email by the link sent on your email address";
    }
}
