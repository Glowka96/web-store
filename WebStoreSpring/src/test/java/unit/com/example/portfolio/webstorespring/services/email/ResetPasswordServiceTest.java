package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.confirmations.AccountConfTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.function.Consumer;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountConfTokenBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock
    private AccountConfTokenService accountConfTokenService;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private ResetPasswordService underTest;

    @Test
    void shouldSendResetPasswordLink() {
        Account account = make(a(BASIC_ACCOUNT));
        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));
        Map<String, Object> excepted = Map.of("message", "Sent reset password link to your email");

        given(accountService.findByEmail(anyString())).willReturn(account);
        given(accountConfTokenService.create(any(Account.class))).willReturn(accountConfToken);

        Map<String, Object> result = underTest.resetPasswordByEmail(account.getEmail());

        assertEquals(excepted, result);
        verify(emailSenderService, times(1)).sendEmail(any(NotificationType.class), anyString(), anyString());
    }

    @Test
    void shouldConfirmResetPassword() {
        Account account = make(a(BASIC_ACCOUNT));
        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123*");

        Map<String, Object> excepted = Map.of("message", "Your new password has been saved");

        given(accountConfTokenService.confirmTokenAndExecute(anyString(), any(), anyString()))
                .willReturn(Map.of("message", "Your new password has been saved"));

        Map<String, Object> result = underTest.confirmResetPassword(resetPasswordRequest, accountConfToken.getToken());

        assertEquals(excepted, result);
        ArgumentCaptor<Consumer<Account>> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(accountConfTokenService).confirmTokenAndExecute(eq(accountConfToken.getToken()), consumerCaptor.capture(), eq("Your new password has been saved"));

        Consumer<Account> capturedConsumer = consumerCaptor.getValue();
        capturedConsumer.accept(account);
        verify(accountService).setNewAccountPassword(account, resetPasswordRequest.password());
    }
}
