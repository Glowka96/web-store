package com.example.portfolio.webstorespring;

import com.example.portfolio.webstorespring.config.JpaAuditingConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
@Import(JpaAuditingConfig.class)
public class ContainersConfig {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mySQLContainer() {
        return new MySQLContainer<>("mysql:8.0.31");
    }
}
