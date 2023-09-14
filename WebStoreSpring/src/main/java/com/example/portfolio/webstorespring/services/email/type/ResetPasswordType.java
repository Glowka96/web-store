package com.example.portfolio.webstorespring.services.email.type;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public enum ResetPasswordType implements EmailTypeStrategy {

    PASSWORD();

    @Value("${reset-password.confirmation.link}")
    private String confirmLink;

    private final String title;
    private final String emailMessageWithLink;
    private final String informationMessage;

    ResetPasswordType() {
        this.title = "Complete reset password!";
        this.emailMessageWithLink = "To reset your password, please click here: \n" + confirmLink;
        this.informationMessage = "Sent reset password link on your email address";
    }
}
