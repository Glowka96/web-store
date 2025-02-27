package com.example.portfolio.webstorespring.configs;

import com.example.portfolio.webstorespring.repositories.subscribers.InitSubscriptionData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitSubscriptionConfig {

    @Bean
    public InitSubscriptionData initSubscriptionData() {
        return new InitSubscriptionData();
    }
}
