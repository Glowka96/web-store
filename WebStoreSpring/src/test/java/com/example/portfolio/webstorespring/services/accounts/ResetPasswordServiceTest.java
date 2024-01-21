package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.enums.emailtypes.ResetPasswordType;
import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.createAccountWithRoleUser;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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

    @Test
    void shouldResetPassword() {
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account);

        Map<String, Object> excepted = Map.of("message", ResetPasswordType.PASSWORD.getInformationMessage());

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(account));
        given(confirmationTokenService.createConfirmationToken(any(Account.class))).willReturn(confirmationToken);
        given(emailSenderService.sendEmail(anyString(), anyString())).willReturn(excepted);

        // when
        Map<String, Object> result = underTest.resetPasswordByEmail(account.getEmail());

        // then
        assertThat(result).isEqualTo(excepted);

    }

    @Test
    void shouldConfirmResetPassword() {
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account);
        String password = "Password123*";

        Map<String, Object> excepted = Map.of("message", "Your new password has been saved");

        given(confirmationTokenService.getConfirmationTokenByToken(anyString())).willReturn(confirmationToken);
        given(encoder.encode(anyString())).willReturn(password);

        // when
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
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account);

        // when
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isConfirmed(any(ConfirmationToken.class))).thenReturn(true);

        // then
        assertThatThrownBy(() -> underTest.confirmResetPassword("Password123*", confirmationToken.getToken()))
                .isInstanceOf(TokenConfirmedException.class)
                .hasMessageContaining("This token is confirmed.");
    }

    @Test
    void willThrowWhenTokenIsExpired() {
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account);

        // when
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).thenReturn(true);

        // then
        assertThatThrownBy(() -> underTest.confirmResetPassword("Password123*", confirmationToken.getToken()))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessageContaining("This token is expired.");
    }
}
