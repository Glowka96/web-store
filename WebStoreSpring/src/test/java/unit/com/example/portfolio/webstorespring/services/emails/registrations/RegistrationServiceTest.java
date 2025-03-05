package com.example.portfolio.webstorespring.services.emails.registrations;

import com.example.portfolio.webstorespring.enums.EmailType;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.services.accounts.AccountService;
import com.example.portfolio.webstorespring.services.emails.EmailSenderService;
import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.RegistrationRequestBuilderHelper.createRegistrationRequest;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private AccountConfTokenService confTokenService;
    @InjectMocks
    private RegistrationService underTest;

    @Test
    void shouldRegister() {
        RegistrationRequest request = createRegistrationRequest();
        Account account = make(a(BASIC_ACCOUNT));
        ResponseMessageDTO excepted = new ResponseMessageDTO("Verify your email address using the link in your email.");
        AccountConfToken accountConfToken = mock(AccountConfToken.class);

        given(accountConfToken.getToken()).willReturn("token123");
        given(accountService.save(any(RegistrationRequest.class))).willReturn(account);
        given(confTokenService.create(any(Account.class), any(EmailType.class))).willReturn(accountConfToken);

        ResponseMessageDTO result = underTest.register(request);

        assertNotNull(result);
        assertEquals(excepted, result);
        verify(emailSenderService, times(1)).sendEmail(eq(EmailType.CONFIRM_EMAIL), eq(account.getEmail()), anyString());

    }
}