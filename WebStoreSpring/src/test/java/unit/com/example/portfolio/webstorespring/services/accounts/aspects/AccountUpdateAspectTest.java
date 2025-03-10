package com.example.portfolio.webstorespring.services.accounts.aspects;

import com.example.portfolio.webstorespring.exceptions.EmailAlreadyUsedException;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.LoginRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.UpdateEmailRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.UpdatePasswordRequest;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
import com.example.portfolio.webstorespring.services.emails.accountactions.RestoreEmailService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AccountUpdateAspectTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RestoreEmailService restoreEmailService;

    @InjectMocks
    private AccountUpdateAspect underTest;


    @Test
    void shouldValidateEmailUpdate_whenAllDataAreValid() {
        AccountDetails accountDetails = getAccountDetails();
        UpdateEmailRequest updateEmailRequest = getUpdateEmailRequest(accountDetails);

        given(accountRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(Boolean.TRUE);

        assertDoesNotThrow(() -> underTest.beforeValidateEmailUpdate(accountDetails, updateEmailRequest));
    }

    @Test
    void willThrowEmailAlreadyExistsException_whenEmailAlreadyExists() {
        AccountDetails accountDetails = getAccountDetails();
        UpdateEmailRequest updateEmailRequest = getUpdateEmailRequest(accountDetails);

        given(accountRepository.existsByEmail(anyString())).willReturn(Boolean.TRUE);

        assertThrows(EmailAlreadyUsedException.class, () -> underTest.beforeValidateEmailUpdate(accountDetails, updateEmailRequest));
    }

    @Test
    void willThrowBadCredentialsException_whenEmailIsInvalid() {
        AccountDetails accountDetails = getAccountDetails();
        LoginRequest loginRequest = new LoginRequest("invalidEmail@test.pl", accountDetails.getPassword());
        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest("newemail@test.pl", loginRequest);

        given(accountRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);

        assertThrows(BadCredentialsException.class, () -> underTest.beforeValidateEmailUpdate(accountDetails, updateEmailRequest));
    }

    @Test
    void willThrowBadCredentialsException_whenPasswordIsInvalid() {
        AccountDetails accountDetails = getAccountDetails();
        LoginRequest loginRequest = new LoginRequest(accountDetails.getUsername(), "invalidPassword");
        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest("newemail@test.pl", loginRequest);

        given(accountRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(Boolean.FALSE);

        assertThrows(BadCredentialsException.class, () -> underTest.beforeValidateEmailUpdate(accountDetails, updateEmailRequest));
    }

    @Test
    void shouldSendRestoreEmail_afterValidateEmailUpdate() {
        AccountDetails accountDetails = getAccountDetails();

        underTest.afterValidateEmailUpdate(accountDetails, getUpdateEmailRequest(accountDetails));

        verify(restoreEmailService, times(1)).sendRestoreEmail(any(Account.class));
    }

    @Test
    void shouldValidatePasswordUpdate_whenAllDataAreValid() {
        AccountDetails accountDetails = getAccountDetails();
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest(accountDetails.getUsername(), "newPassword132*");

        given(passwordEncoder.matches(anyString(), anyString())).willReturn(Boolean.TRUE);

        underTest.beforeValidatePasswordUpdate(accountDetails, updatePasswordRequest);

        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    private @NotNull AccountDetails getAccountDetails() {
        return new AccountDetails(make(a(BASIC_ACCOUNT)));
    }

    private @NotNull UpdateEmailRequest getUpdateEmailRequest(AccountDetails accountDetails) {
        LoginRequest loginRequest = new LoginRequest(accountDetails.getUsername(), accountDetails.getPassword());
        return new UpdateEmailRequest("newemail@test.pl", loginRequest);
    }
}
