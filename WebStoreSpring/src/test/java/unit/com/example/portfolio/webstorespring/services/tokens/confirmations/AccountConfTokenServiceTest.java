package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.TokenDetails;
import com.example.portfolio.webstorespring.repositories.tokens.confirmations.AccountConfTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Consumer;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.AccountConfTokenBuilderHelper.createAccountConfToken;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.BASIC_TOKEN_DETAILS;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountConfTokenServiceTest {

    @Mock
    private AccountConfTokenRepository accountConfTokenRepository;
    @Mock
    private TokenDetailsService tokenDetailsService;
    @Mock
    private NotificationExpirationManager notificationExpirationManager;
    @InjectMocks
    private AccountConfTokenService underTest;

    private AccountConfToken confToken;
    private TokenDetails tokenDetails;
    private Account account;

    @BeforeEach
    void setup() {
        account = make(a(BASIC_ACCOUNT));
        tokenDetails = make(a(BASIC_TOKEN_DETAILS));
        confToken = createAccountConfToken(account, tokenDetails);
    }

    @Test
    void confirmTokenAndExecute() {
        given(accountConfTokenRepository.findByTokenDetails_Token(anyString())).willReturn(Optional.ofNullable(confToken));

        doNothing().when(tokenDetailsService).validate(tokenDetails);
        doNothing().when(tokenDetailsService).setConfirmedAt(tokenDetails);
        Consumer<Account> confirmationConsumer = mock(Consumer.class);
        String successMessage = "Token confirmed successfully";

        ResponseMessageDTO result = underTest.confirmTokenAndExecute(tokenDetails.getToken(), confirmationConsumer, successMessage);

        assertEquals(successMessage, result.message());
        verify(tokenDetailsService, times(1)).validate(tokenDetails);
        verify(tokenDetailsService, times(1)).setConfirmedAt(tokenDetails);
        verify(confirmationConsumer).accept(account);
    }
}