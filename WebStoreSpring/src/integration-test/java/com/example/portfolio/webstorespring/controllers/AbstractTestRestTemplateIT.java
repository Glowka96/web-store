package com.example.portfolio.webstorespring.controllers;

import com.example.portfolio.webstorespring.ContainersConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ContainersConfig.class)
@Testcontainers
public abstract class AbstractTestRestTemplateIT {

    @Autowired
    protected TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;
    protected static String localhostUri;
    protected static String localhostAdminUri;
    @BeforeEach
    void setupURI() {
        localhostUri = "http://localhost:" + port + "/api/v1";
        localhostAdminUri = localhostUri + "/admin";
    }
}
