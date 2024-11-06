package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.accounts.ConfirmationTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void shouldSendResetPasswordLink() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));
        Map<String, Object> excepted = Map.of("message", "Sent reset password link to your email");

        given(accountService.findAccountByEmail(anyString())).willReturn(account);
        given(confirmationTokenService.createConfirmationToken(any(Account.class))).willReturn(confirmationToken);
        given(emailSenderService.sendEmail(any(NotificationType.class), anyString(), anyString())).willReturn(excepted);

        Map<String, Object> result = underTest.resetPasswordByEmail(account.getEmail());

        assertEquals(excepted, result);
    }

    @Test
    void shouldConfirmResetPassword() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123*");

        Map<String, Object> excepted = Map.of("message", "Your new password has been saved");

        given(confirmationTokenService.getConfirmationTokenByToken(anyString())).willReturn(confirmationToken);
        given(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).willReturn(false);

        Map<String, Object> result = underTest.confirmResetPassword(resetPasswordRequest, confirmationToken.getToken());

        assertEquals(excepted, result);
        verify(confirmationTokenService, times(1)).isTokenExpired(any(ConfirmationToken.class));
        verify(confirmationTokenService, times(1)).setConfirmedAtAndSaveConfirmationToken(any(ConfirmationToken.class));
        verify(accountService, times(1)).setNewAccountPassword(any(Account.class), anyString());
    }

    @Test
    void willThrowTokenConfirmedException_whenTokenIsConfirm() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123*");

        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);

        assertThatThrownBy(() -> underTest.confirmResetPassword(resetPasswordRequest, confirmationToken.getToken()))
                .isInstanceOf(TokenConfirmedException.class)
                .hasMessageContaining("This token is confirmed.");
    }

    @Test
    void willThrowTokenExpiredException_whenTokenIsExpired() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT))
                .but(with(EXPIRED_AT, DateForTestBuilderHelper.LOCAL_DATE_TIME)));
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123*");

        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);
        when(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).thenReturn(true);

        assertThatThrownBy(() -> underTest.confirmResetPassword(resetPasswordRequest, confirmationToken.getToken()))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessageContaining("This token is expired.");
    }
}
