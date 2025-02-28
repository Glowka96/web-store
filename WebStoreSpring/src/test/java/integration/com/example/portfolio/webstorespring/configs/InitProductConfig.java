package com.example.portfolio.webstorespring.configs;

import com.example.portfolio.webstorespring.productsTestData.impl.InitProductTestDataImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitProductConfig {

    @Bean
    public InitProductTestDataImpl initProductTestData() {
        return new InitProductTestDataImpl();
    }
}
