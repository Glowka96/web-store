package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.dto.accounts.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import com.example.portfolio.webstorespring.services.email.type.ConfirmEmailType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
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
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private RegistrationService underTest;

    private ConfirmationToken confirmationToken;
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
        account.setEnabled(false);

        confirmationToken = new ConfirmationToken();
        confirmationToken.setToken("token");
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        confirmationToken.setAccount(account);
    }

    @Test
    void shouldRegistrationAccount() {
        // given
        excepted = Map.of("message", ConfirmEmailType.REGISTRATION.getInformationMessage());
        // when
        when(confirmationTokenService.createConfirmationToken(any(Account.class))).thenReturn(confirmationToken);
        when(emailSenderService.sendEmail(anyString(), anyString()))
                .thenReturn(excepted);

        Map<String, Object> result = underTest.registrationAccount(request);

        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        Account captureAccount = accountArgumentCaptor.getValue();

        // then
        assertThat(result).isEqualTo(excepted);
        verify(accountRepository).save(captureAccount);
        verify(emailSenderService).sendEmail(account.getEmail(),
                confirmationToken.getToken());
    }

    @Test
    void shouldSuccessConfirmToken() {
        // given
        excepted = Map.of("message","Account confirmed");
        // when
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);

        Map<String, Object> result = underTest.confirmToken(confirmationToken.getToken());

        // then
        verify(confirmationTokenService, times(1)).setConfirmedAtAndSaveConfirmationToken(confirmationToken);
        verify(accountRepository, times(1)).save(account);
        assertThat(result).isEqualTo(excepted);
    }

    @Test
    void willThrowWhenAccountConfirm() {
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isConfirmed(any(ConfirmationToken.class))).thenReturn(true);

        assertThatThrownBy(() -> underTest.confirmToken(confirmationToken.getToken()))
                .isInstanceOf(EmailAlreadyConfirmedException.class)
                .hasMessageContaining("Email already confirmed");
    }

    @Test
    void shouldSendEmailWhenTokenIsExpired() {
        // given
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).thenReturn(true);
        when(confirmationTokenService.createConfirmationToken(any(Account.class))).thenReturn(confirmationToken);
        when(emailSenderService.sendEmail(anyString(), anyString()))
                .thenReturn(excepted);

        Map<String, Object> result = underTest.confirmToken(confirmationToken.getToken());

        // then
        assertThat(result).isEqualTo(excepted);
    }
}
