package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
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

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.ENABLED;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.RegistrationRequestBuilderHelper.createRegistrationRequest;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private RegistrationService underTest;

    @Test
    void shouldRegistrationAccount() {
        RegistrationRequest registrationRequest = createRegistrationRequest();
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));
        Map<String, Object> excepted = Map.of("message", "Verify your email address using the link in your email.");

        given(confirmationTokenService.create(any(Account.class))).willReturn(confirmationToken);
        given(accountService.save(registrationRequest)).willReturn(account);

        Map<String, Object> result = underTest.registrationAccount(registrationRequest);

        assertEquals(excepted, result);
        verify(emailSenderService, times(1)).sendEmail(any(NotificationType.class), anyString(), anyString());
    }

    @Test
    void shouldSuccessConfirmToken() {
        Account account = make(a(BASIC_ACCOUNT)
                .but(with(ENABLED, Boolean.FALSE)));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));
        Map<String, Object> excepted = Map.of("message","Account confirmed.");

        when(confirmationTokenService.getByToken(anyString())).thenReturn(confirmationToken);

        Map<String, Object> result = underTest.confirmToken(confirmationToken.getToken());

        verify(confirmationTokenService, times(1)).setConfirmedAt(any(ConfirmationToken.class));
        verify(accountService, times(1)).setEnabledAccount(any(Account.class));
        assertEquals(excepted, result);
    }

    @Test
    void willThrowEmailAlreadyConfirmedException_whenAccountIsConfirm() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));
        when(confirmationTokenService.getByToken(anyString())).thenReturn(confirmationToken);

        assertThatThrownBy(() -> underTest.confirmToken(confirmationToken.getToken()))
                .isInstanceOf(EmailAlreadyConfirmedException.class)
                .hasMessageContaining("Email already confirmed.");
    }

    @Test
    void shouldSendEmail_whenTokenIsExpired() {
        Account account = make(a(BASIC_ACCOUNT));
        account.setEnabled(false);
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT))
                .but(with(EXPIRED_AT, LOCAL_DATE_TIME)));
        Map<String, Object> excepted = Map.of("message",
                "Your token is expired. Verify your email address using the new token link in your email.");

        given(confirmationTokenService.getByToken(anyString())).willReturn(confirmationToken);
        given(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).willReturn(true);
        given(confirmationTokenService.create(any(Account.class))).willReturn(confirmationToken);

        Map<String, Object> result = underTest.confirmToken(confirmationToken.getToken());

        assertEquals(excepted, result);
        verify(confirmationTokenService, times(1)).delete(any(ConfirmationToken.class));
        verify(emailSenderService, times(1)).sendEmail(any(NotificationType.class), anyString(), anyString());
    }
}
