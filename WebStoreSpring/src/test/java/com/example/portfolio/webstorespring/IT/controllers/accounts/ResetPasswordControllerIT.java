package com.example.portfolio.webstorespring.IT.controllers.accounts;


import com.example.portfolio.webstorespring.IT.controllers.AbstractIT;
import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.ConfirmationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class ResetPasswordControllerIT extends AbstractIT {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ConfirmationTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    private String resetPasswordUri;
    private Account savedAccount;

    @BeforeEach
    public void initTestData() {
        resetPasswordUri = localhostUri + "/reset-password";
        accountRepository.deleteAll();
        savedAccount = accountRepository.save(make(a(AccountBuilderHelper.BASIC_ACCOUNT)));
    }

    @Test
    void shouldResetPassword_forEverybody_thenStatusOk() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                resetPasswordUri + "?email=" + savedAccount.getEmail(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertTrue(Objects.requireNonNull(response.getBody())
                .containsValue("Sent reset password link in your email"));
    }

    @Test
    void shouldConfirmResetPassword_withValidToken_thenStatusAccepted() {
        ConfirmationToken savedConfirmationToken = tokenRepository.save(
                make(a(BASIC_CONFIRMATION_TOKEN)
                        .but(with(ACCOUNT, savedAccount))
                        .but(with(CREATED_AT, LocalDateTime.now()))
                        .but(withNull(CONFIRMED_AT))
                        .but(with(EXPIRED_AT, LocalDateTime.now().plusMinutes(15))))
        );
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("newPassword123*");

        HttpEntity<ResetPasswordRequest> requestEntity = new HttpEntity<>(resetPasswordRequest);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                resetPasswordUri + "/confirm?token=" + savedConfirmationToken.getToken(),
                HttpMethod.PATCH,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertTrue(Objects.requireNonNull(response.getBody())
                .containsValue("Your new password has been saved"));

        Optional<Account> optionalAccount = accountRepository.findByEmail(savedAccount.getEmail());
        assertThat(optionalAccount).isPresent();
        assertTrue(encoder.matches("newPassword123*", optionalAccount.get().getPassword()));
    }
}
