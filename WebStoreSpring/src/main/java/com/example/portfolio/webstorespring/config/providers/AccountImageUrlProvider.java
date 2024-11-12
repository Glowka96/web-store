package com.example.portfolio.webstorespring.config.providers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "account.image")
@Getter
@Setter
public class AccountImageUrlProvider {

    private String url;
}
