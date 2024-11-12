package com.example.portfolio.webstorespring.config.providers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "confirmation.link")
@Getter
@Setter
public class ConfirmationLinkProvider {

    private String email;
    private String resetPassword;
}
