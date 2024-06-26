package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.enums.emailtypes.ConfirmEmailType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
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
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationTokenIsConfirmedAt;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationTokenIsNotConfirmedAt;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.RegistrationRequestBuilderHelper.createRegistrationRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        // given
        RegistrationRequest registrationRequest = createRegistrationRequest();
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationTokenIsNotConfirmedAt(account);

        Map<String, Object> excepted = Map.of("message", ConfirmEmailType.REGISTRATION.getInformationMessage());

        given(confirmationTokenService.createConfirmationToken(any(Account.class))).willReturn(confirmationToken);
        given(emailSenderService.sendEmail(anyString(), anyString()))
                .willReturn(excepted);
        given(accountService.saveAccount(registrationRequest)).willReturn(account);

        // when
        Map<String, Object> result = underTest.registrationAccount(registrationRequest);

        // then
        assertThat(result).isEqualTo(excepted);
    }

    @Test
    void shouldSuccessConfirmToken() {
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationTokenIsNotConfirmedAt(account);

        Map<String, Object> excepted = Map.of("message","Account confirmed");

        // when
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);

        Map<String, Object> result = underTest.confirmToken(confirmationToken.getToken());

        // then
        verify(confirmationTokenService, times(1)).setConfirmedAtAndSaveConfirmationToken(any(ConfirmationToken.class));
        verify(accountService, times(1)).setEnabledAccount(any(Account.class));
        assertThat(result).isEqualTo(excepted);
    }

    @Test
    void willThrowWhenAccountConfirm() {
        // given
        Account account = createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationTokenIsConfirmedAt(account);

        // when
        when(confirmationTokenService.getConfirmationTokenByToken(anyString())).thenReturn(confirmationToken);

        // then
        assertThatThrownBy(() -> underTest.confirmToken(confirmationToken.getToken()))
                .isInstanceOf(EmailAlreadyConfirmedException.class)
                .hasMessageContaining("Email already confirmed");
    }

    @Test
    void shouldSendEmailWhenTokenIsExpired() {
        // given
        Account account = createAccountWithRoleUser();
        account.setEnabled(false);
        ConfirmationToken confirmationToken = createConfirmationTokenIsNotConfirmedAt(account);
        Map<String, Object> excepted = Map.of("message", ConfirmEmailType.REGISTRATION.getInformationMessage());

        given(confirmationTokenService.getConfirmationTokenByToken(anyString())).willReturn(confirmationToken);
        given(confirmationTokenService.isTokenExpired(any(ConfirmationToken.class))).willReturn(true);
        given(confirmationTokenService.createConfirmationToken(any(Account.class))).willReturn(confirmationToken);
        given(emailSenderService.sendEmail(anyString(), anyString()))
                .willReturn(excepted);

        // when
        Map<String, Object> result = underTest.confirmToken(confirmationToken.getToken());

        // then
        assertThat(result).isEqualTo(excepted);
        verify(confirmationTokenService, times(1)).deleteConfirmationToken(any(ConfirmationToken.class));
    }
}
