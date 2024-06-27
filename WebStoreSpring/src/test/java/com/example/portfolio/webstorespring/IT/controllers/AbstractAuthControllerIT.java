package com.example.portfolio.webstorespring.IT.controllers;

import com.example.portfolio.webstorespring.IT.ContainersConfig;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import com.example.portfolio.webstorespring.services.authentication.AuthService;
import com.example.portfolio.webstorespring.services.authentication.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ContainersConfig.class)
@Testcontainers
@Slf4j
public abstract class AbstractAuthControllerIT {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository repository;
    @Autowired
    private AuthService authService;
    @LocalServerPort
    private Integer port;
    private static String ADMIN_TOKEN;
    private static String USER_TOKEN;
    protected static String LOCALHOST_URI;

    @BeforeEach
    public void init() {
        Set<Role> userRole = repository.findByName("ROLE_USER");
        Set<Role> adminRole = repository.findByName("ROLE_ADMIN");
        Account user = make(a(BASIC_ACCOUNT).but(with(EMAIL, "user@test.pl"),
                with(ROLES, userRole), with(PASSWORD, passwordEncoder.encode("Password123*"))));
        Account admin = make(a(BASIC_ACCOUNT).but(
                with(EMAIL, "admin@test.pl"),
                with(PASSWORD, passwordEncoder.encode("Password123*")),
                with(ROLES, adminRole)));

        accountRepository.deleteAll();
        Account savedUser = accountRepository.save(user);
        Account savedAdmin = accountRepository.save(admin);

        USER_TOKEN = jwtService.generateToken(new AccountDetails(savedUser));
        ADMIN_TOKEN = jwtService.generateToken(new AccountDetails(savedAdmin));
        authService.saveAccountAuthToken(savedUser, USER_TOKEN);
        authService.saveAccountAuthToken(savedAdmin, ADMIN_TOKEN);

        LOCALHOST_URI = "http://localhost:" + port;
    }

    public static HttpHeaders getHttpHedearsWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_TOKEN);
        return headers;
    }

    public static HttpHeaders getHttpHeaderWithUserToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(USER_TOKEN);
        return headers;
    }
}
