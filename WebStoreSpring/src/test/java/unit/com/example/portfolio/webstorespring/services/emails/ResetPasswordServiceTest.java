package com.example.portfolio.webstorespring.services.emails;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.accounts.request.ResetPasswordRequest;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.AccountConfTokenBuilderHelper.createAccountConfToken;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.*;
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
        AccountConfToken accountConfToken =
                createAccountConfToken(
                        account,
                        make(a(BASIC_TOKEN_DETAILS)
                                .but(with(CREATED_AT, LocalDateTime.now()))
                                .but(withNull(CONFIRMED_AT))
                                .but(with(EXPIRES_AT, LocalDateTime.now().plusDays(7)))
                        )
                );

        ResponseMessageDTO excepted = new ResponseMessageDTO("Sent reset password link to your email");

        given(accountService.findByEmail(anyString())).willReturn(account);
        given(accountConfTokenService.create(any(Account.class), any(EmailType.class))).willReturn(accountConfToken);

        ResponseMessageDTO result = underTest.sendResetPasswordLinkByEmail(account.getEmail());

        assertEquals(excepted, result);
        verify(emailSenderService, times(1)).sendEmail(any(EmailType.class), anyString(), anyString());
    }

    @Test
    void shouldConfirmResetPassword() {
        Account account = make(a(BASIC_ACCOUNT));
        AccountConfToken accountConfToken =
                createAccountConfToken(
                        account,
                        make(a(BASIC_TOKEN_DETAILS)
                                .but(with(CREATED_AT, LocalDateTime.now()))
                                .but(withNull(CONFIRMED_AT))
                                .but(with(EXPIRES_AT, LocalDateTime.now().plusDays(7)))
                        )
                );
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("Password123*");

        ResponseMessageDTO excepted = new ResponseMessageDTO("Your new password has been saved");

        given(accountConfTokenService.confirmTokenAndExecute(anyString(), any(), anyString()))
                .willReturn(new ResponseMessageDTO("Your new password has been saved"));

        ResponseMessageDTO result = underTest.confirm(resetPasswordRequest, accountConfToken.getToken());

        assertEquals(excepted, result);
        ArgumentCaptor<Consumer<Account>> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(accountConfTokenService).confirmTokenAndExecute(eq(accountConfToken.getToken()), consumerCaptor.capture(), eq("Your new password has been saved"));

        Consumer<Account> capturedConsumer = consumerCaptor.getValue();
        capturedConsumer.accept(account);
        verify(accountService).setNewAccountPassword(account, resetPasswordRequest.password());
    }
}
