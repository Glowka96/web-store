package com.example.portfolio.webstorespring.services.email;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.accounts.ConfirmationTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.function.Consumer;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.BASIC_CONFIRMATION_TOKEN;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.EXPIRED_AT;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RestoreEmailServiceTest {

    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private RestoreEmailService underTest;


    @Test
    void shouldSendRestoreEmail() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(EXPIRED_AT, LOCAL_DATE_TIME.plusDays(7)))
        );

        given(confirmationTokenService.createWith7DaysExpires(any(Account.class))).willReturn(confirmationToken);

        underTest.sendRestoreEmail(account);

        verify(emailSenderService, times(1)).sendEmail(
                NotificationType.RESTORE_EMAIL,
                account.getEmail(),
                confirmationToken.getToken()
        );
    }

    @Test
    void shouldConfirmRestoreEmail() {
        String message = "Old account email restored";
        String oldEmail = "oldEmail@test.pl";

        Account account = make(a(BASIC_ACCOUNT)
                .but(with(EMAIL, "newEmail@test.pl"))
                .but(with(BACKUPEMAIL, oldEmail)));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN));


        given(confirmationTokenService.confirmTokenAndExecute(anyString(), any(), anyString()))
                .willReturn(Map.of("message", message));

        Map<String, Object> result = underTest.confirmRestoreEmail(confirmationToken.getToken());

        assertEquals(message, result.get("message"));
        ArgumentCaptor<Consumer<Account>> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(confirmationTokenService).confirmTokenAndExecute(eq(confirmationToken.getToken()), consumerCaptor.capture(), eq(message));

        Consumer<Account> consumer = consumerCaptor.getValue();
        consumer.accept(account);
        verify(accountService, times(1)).restoreEmail(account);
    }


}
