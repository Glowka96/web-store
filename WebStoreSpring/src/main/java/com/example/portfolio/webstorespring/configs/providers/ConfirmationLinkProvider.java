package com.example.portfolio.webstorespring.configs.providers;

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
    private String restoreEmail;
    private String newsletter;
    private String productSubscription;
    private String unsubscribeNewsletter;
    private String unsubscribeProductSubscription;
    private String unsubscribeSingleProduct;
}
