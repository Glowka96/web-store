package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.porfolio.webstorespring.model.dto.accounts.RegistrationRequest;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.porfolio.webstorespring.services.accounts.ConfirmationTokenService;
import com.example.porfolio.webstorespring.services.accounts.EmailSenderConfiguration;
import com.example.porfolio.webstorespring.services.accounts.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private ConfirmationTokenService tokenService;

    @Mock
    private EmailSenderConfiguration emailSenderConfiguration;

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private RegistrationService underTest;

    private ConfirmationToken token;
    private Account account;
    private RegistrationRequest request;
    private Map<String, Object> excepted;

    @BeforeEach
    void initialization() {
        request = new RegistrationRequest();
        request.setFirstName("Test");
        request.setLastName("Test");
        request.setEmail("test@test.pl");
        request.setPassword("password");

        account = new Account();
        account.setEmail(request.getEmail());

        token = new ConfirmationToken();
        token.setToken("token");
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(1));
        token.setAccount(account);

        excepted = new HashMap<>();
    }

    @Test
    void shouldRegistrationAccount() {
        // given
        excepted.put("message", "Verify email by the link sent on your email address");
        // when
        when(tokenService.createConfirmationToken(any(Account.class))).thenReturn(token);
        when(emailSenderConfiguration.sendEmail(anyString(), anyString(), anyString()))
                .thenReturn(excepted);

        Map<String, Object> result = underTest.registrationAccount(request);

        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        Account captureAccount = accountArgumentCaptor.getValue();

        // then
        assertThat(result).isEqualTo(excepted);
        verify(accountRepository).save(captureAccount);
        verify(emailSenderConfiguration).sendEmail(account.getEmail(),
                "Complete Registration!",
                token.getToken());
    }

    @Test
    void shouldSuccessConfirmToken() {
        // given
        excepted.put("message","Account confirmed");
        // when
        token.setConfirmedAt(LocalDateTime.now());
        when(tokenService.getConfirmationTokenByToken(anyString())).thenReturn(token);

        Map<String, Object> result = underTest.confirmToken(token.getToken());

        // then
        verify(tokenService, times(1)).setConfirmedAtAndSaveConfirmationToken(token);
        verify(accountRepository, times(1)).save(account);
        assertThat(result).isEqualTo(excepted);
    }

    @Test
    void willThrowWhenAccountConfirm() {
        when(tokenService.getConfirmationTokenByToken(anyString())).thenReturn(token);
        when(tokenService.isConfirmed(any(ConfirmationToken.class))).thenReturn(true);

        assertThatThrownBy(() -> underTest.confirmToken(token.getToken()))
                .isInstanceOf(EmailAlreadyConfirmedException.class)
                .hasMessageContaining("Email already confirmed");
    }

    @Test
    void shouldSendEmailWhenTokenIsExpired() {
        // given
        when(tokenService.getConfirmationTokenByToken(anyString())).thenReturn(token);
        when(tokenService.isTokenExpired(any(ConfirmationToken.class))).thenReturn(true);
        when(tokenService.createConfirmationToken(any(Account.class))).thenReturn(token);
        when(emailSenderConfiguration.sendEmail(anyString(), anyString(), anyString()))
                .thenReturn(excepted);

        Map<String, Object> result = underTest.confirmToken(token.getToken());

        // then
        assertThat(result).isEqualTo(excepted);
    }
}
