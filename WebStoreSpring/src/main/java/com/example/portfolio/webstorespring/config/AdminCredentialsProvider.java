package com.example.portfolio.webstorespring.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "admin")
@Getter
@Setter
public class AdminCredentialsProvider {

    private String email;
    private String password;
}
