package com.example.portfolio.webstorespring.IT.controllers.accounts;


import com.example.portfolio.webstorespring.IT.ContainersConfig;
import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.ConfirmationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationTokenIsNotConfirmedAt;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ContainersConfig.class)
@Testcontainers
class ResetPasswordControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ConfirmationTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    @LocalServerPort
    private Integer port;
    private String uri;
    private Account savedAccount;

    @BeforeEach
    void setup() {
        uri = "http://localhost:" + port + "/api/v1/reset-password";
        accountRepository.deleteAll();
        savedAccount = accountRepository.save(make(a(AccountBuilderHelper.BASIC_ACCOUNT)));
    }

    @Test
    void shouldResetPassword_forEverybody_thenStatusOk() {
        String requestUri = uri + "?email=" + savedAccount.getEmail();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                requestUri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertTrue(Objects.requireNonNull(response.getBody()).containsValue("Sent reset password link on your email address"));
    }

    @Test
    void shouldConfirmResetPassword_withValidToken_thenStatusAccepted() {
        ConfirmationToken savedConfirmationToken =
                tokenRepository.save(createConfirmationTokenIsNotConfirmedAt(savedAccount, LocalDateTime.now()));
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("newPassword123*");

        String requestUri = uri + "/confirm?token=" + savedConfirmationToken.getToken();
        HttpEntity<ResetPasswordRequest> requestEntity = new HttpEntity<>(resetPasswordRequest);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                requestUri,
                HttpMethod.PATCH,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertTrue(Objects.requireNonNull(response.getBody()).containsValue("Your new password has been saved"));

        Optional<Account> optionalAccount = accountRepository.findByEmail(savedAccount.getEmail());
        assertThat(optionalAccount).isPresent();
        assertTrue(encoder.matches("newPassword123*", optionalAccount.get().getPassword()));
    }
}