package com.example.portfolio.webstorespring.controllers;

import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.models.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import com.example.portfolio.webstorespring.services.authentication.AuthService;
import com.example.portfolio.webstorespring.services.authentication.JwtService;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;

public abstract class AbstractAuthControllerIT extends AbstractTestRestTemplateIT {

    @Autowired
    private JwtService jwtService;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected AccountAddressRepository addressRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthService authService;
    private String adminToken;
    private String userToken;
    @Getter
    private Account savedUser;
    @Getter
    private AccountAddress savedAddress;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    protected void init() {
        Set<Role> userRole = roleRepository.findByName("ROLE_USER");
        Set<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");

        Account user = createAccount("user@test.pl",userRole);
        Account admin = createAccount("admin@test.p", adminRole);

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            savedUser = accountRepository.saveAndFlush(user);
            AccountAddress address = AccountAddressBuilderHelper.createAccountAddress();
            address.setId(null);
            address.setAccount(savedUser);

            savedAddress = addressRepository.saveAndFlush(address);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
        Account savedAdmin = accountRepository.saveAndFlush(admin);

        userToken = jwtService.generateToken(new AccountDetails(savedUser));
        adminToken = jwtService.generateToken(new AccountDetails(savedAdmin));

        authService.saveAccountAuthToken(savedUser, userToken);
        authService.saveAccountAuthToken(savedAdmin, adminToken);
    }

    @AfterEach
    protected void delete() {
        addressRepository.deleteAll();
        accountRepository.deleteAll();
    }

    private Account createAccount(String email, Set<Role> accountRoles) {
        return make(a(BASIC_ACCOUNT).but(
                withNull(ID),
                with(EMAIL, email),
                with(ROLES, accountRoles),
                with(PASSWORD, passwordEncoder.encode("Password123*"))));
    }

    public HttpHeaders getHttpHeadersWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        return headers;
    }

    public HttpHeaders getHttpHeaderWithUserToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        return headers;
    }
}
