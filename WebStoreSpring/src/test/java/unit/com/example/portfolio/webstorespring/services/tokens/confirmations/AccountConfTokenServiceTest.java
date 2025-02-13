package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.enums.NotificationType;
import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.models.dto.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.AccountConfToken;
import com.example.portfolio.webstorespring.models.entity.tokens.confirmations.TokenDetails;
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
import static org.junit.jupiter.api.Assertions.*;
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
    void shouldGetByToken() {
        given(accountConfTokenRepository.findByTokenDetails_Token(anyString())).willReturn(Optional.of(confToken));

        AccountConfToken result = underTest.getByToken("token123");

        assertNotNull(result);
        assertEquals(confToken, result);
    }

    @Test
    void shouldCreate() {
        given(notificationExpirationManager.getExpirationMinutes(any(NotificationType.class))).willReturn(15L);
        given(tokenDetailsService.create(anyLong())).willReturn(tokenDetails);
        given(accountConfTokenRepository.save(any(AccountConfToken.class))).willReturn(confToken);

        AccountConfToken result = underTest.create(account, NotificationType.CONFIRM_EMAIL);

        assertNotNull(result);
        assertEquals(confToken, result);
    }

    @Test
    void shouldDelete() {
        underTest.delete(confToken);

        verify(accountConfTokenRepository, times(1)).delete(confToken);
    }

    @Test
    void willThrowEmailAlreadyConfirmedException_whenTokenIsConfirmedOrOwnerIsEnabled() {
        assertThrows(EmailAlreadyConfirmedException.class, () -> underTest.validateTokenConfirmedOrOwnerEnabled(confToken, confToken.getAccount()));
    }

    @Test
    void shouldReturnTrue_WhenOwnerIsDisabledAndTokenIsExpired() {
        account.setEnabled(Boolean.FALSE);
        given(tokenDetailsService.isTokenExpired(any(TokenDetails.class))).willReturn(Boolean.TRUE);

        boolean result = underTest.isOwnerDisabledAndTokenExpired(account, confToken);

        assertTrue(result);
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