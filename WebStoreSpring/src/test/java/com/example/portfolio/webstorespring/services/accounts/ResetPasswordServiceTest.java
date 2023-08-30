package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import com.example.portfolio.webstorespring.services.email.type.ResetPasswordType;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private ResetPasswordService underTest;

    private ConfirmationToken confirmationToken;
    private Account account;
    private String password;
    private Map<String, Object> excepted;

    @BeforeEach
    void initialization() {
        password = "Test123*";

        account = new Account();
        account.setEmail("test@test.pl");
        account.setPassword("password");

        confirmationToken = new ConfirmationToken();
        confirmationToken.setToken("token");
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        confirmationToken.setAccount(account);
    }

    @Test
    void shouldResetPassword() {
        // given
        excepted = Map.of("message", ResetPasswordType.PASSWORD.getMessage());

        // when
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(account));
        when(confirmationTokenService.createConfirmationToken(any(Account.class))).thenReturn(confirmationToken);
        when(emailSenderService.sendEmail(anyString(), anyString())).thenReturn(excepted);

        Map<String, Object> result = underTest.resetPasswordByEmail(account.getEmail());

        // then
        assertThat(result).isEqualTo(excepted);

    }

    @Test
    void shouldConfirmResetPassword() {
        // given
        excepted = Map.of("message", "Your new password has been saved");

        // when
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(encoder.encode(anyString())).thenReturn(password);

        Map<String, Object> result = underTest.confirmResetPassword(password, confirmationToken.getToken());

        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        Account captureAccount = accountArgumentCaptor.getValue();

        // then
        assertThat(result).isEqualTo(excepted);
        assertThat(captureAccount.getPassword()).isEqualTo(password);
        verify(accountRepository).save(captureAccount);
    }

    @Test
    void willThrowWhenTokenIsConfirm() {
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isConfirmed(any(ConfirmationToken.class))).thenReturn(true);

        assertThatThrownBy(() -> underTest.confirmResetPassword(password, confirmationToken.getToken()))
                .isInstanceOf(TokenConfirmedException.class)
                .hasMessageContaining("This token is confirmed.");
    }

    @Test
    void willThrowWhenTokenIsExpired() {
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).thenReturn(true);

        assertThatThrownBy(() -> underTest.confirmResetPassword(password, confirmationToken.getToken()))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessageContaining("This token is expired.");
    }
}
