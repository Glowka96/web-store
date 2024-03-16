package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.enums.emailtypes.ResetPasswordType;
import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.createAccountWithRoleUser;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private ResetPasswordService underTest;

    @Test
    void shouldResetPassword() {
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account);

        Map<String, Object> excepted = Map.of("message", ResetPasswordType.PASSWORD.getInformationMessage());

        given(accountService.findAccountByEmail(anyString())).willReturn(account);
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
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123*");

        Map<String, Object> excepted = Map.of("message", "Your new password has been saved");

        given(confirmationTokenService.getConfirmationTokenByToken(anyString())).willReturn(confirmationToken);
        given(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).willReturn(false);

        // when
        Map<String, Object> result = underTest.confirmResetPassword(resetPasswordRequest, confirmationToken.getToken());

        // then
        assertThat(result).isEqualTo(excepted);
        verify(confirmationTokenService, times(1)).isConfirmed(any(ConfirmationToken.class));
        verify(confirmationTokenService, times(1)).isTokenExpired(any(ConfirmationToken.class));
        verify(confirmationTokenService, times(1)).setConfirmedAtAndSaveConfirmationToken(any(ConfirmationToken.class));
        verify(accountService, times(1)).setNewAccountPassword(any(Account.class), anyString());
    }

    @Test
    void willThrowWhenTokenIsConfirm() {
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account);
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123*");

        // when
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isConfirmed(any(ConfirmationToken.class))).thenReturn(true);

        // then
        assertThatThrownBy(() -> underTest.confirmResetPassword(resetPasswordRequest, confirmationToken.getToken()))
                .isInstanceOf(TokenConfirmedException.class)
                .hasMessageContaining("This token is confirmed.");
    }

    @Test
    void willThrowWhenTokenIsExpired() {
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account);
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123*");

        // when
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).thenReturn(true);

        // then
        assertThatThrownBy(() -> underTest.confirmResetPassword(resetPasswordRequest, confirmationToken.getToken()))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessageContaining("This token is expired.");
    }
}
