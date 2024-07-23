package com.example.portfolio.webstorespring.IT.controllers;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import com.example.portfolio.webstorespring.services.authentication.AuthService;
import com.example.portfolio.webstorespring.services.authentication.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;

public abstract class AbstractAuthControllerIT extends AbstractIT {

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
    private static String adminToken;
    private static String userToken;

    @BeforeEach
    protected void init() {
        Set<Role> userRole = repository.findByName("ROLE_USER");
        Set<Role> adminRole = repository.findByName("ROLE_ADMIN");
        Account user = make(a(BASIC_ACCOUNT).but(
                withNull(ID),
                with(EMAIL, "user@test.pl"),
                with(ROLES, userRole),
                with(PASSWORD, passwordEncoder.encode("Password123*"))));
        Account admin = make(a(BASIC_ACCOUNT).but(
                withNull(ID),
                with(EMAIL, "admin@test.pl"),
                with(PASSWORD, passwordEncoder.encode("Password123*")),
                with(ROLES, adminRole)));

        accountRepository.deleteAll();
        Account savedUser = accountRepository.save(user);
        Account savedAdmin = accountRepository.save(admin);

        userToken = jwtService.generateToken(new AccountDetails(savedUser));
        adminToken = jwtService.generateToken(new AccountDetails(savedAdmin));
        authService.saveAccountAuthToken(savedUser, userToken);
        authService.saveAccountAuthToken(savedAdmin, adminToken);
    }

    public static HttpHeaders getHttpHeadersWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        return headers;
    }

    public static HttpHeaders getHttpHeaderWithUserToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        return headers;
    }
}
