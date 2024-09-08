package com.example.portfolio.webstorespring.IT.productsTestData;

import com.example.portfolio.webstorespring.IT.productsTestData.impl.InitProductTestDataImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitProductConfig {

    @Bean
    public InitProductTestDataImpl initProductTestData() {
        return new InitProductTestDataImpl();
    }
}
