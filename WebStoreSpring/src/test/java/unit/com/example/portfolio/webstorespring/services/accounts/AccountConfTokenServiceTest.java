//package com.example.portfolio.webstorespring.services.accounts;
//
//import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
//import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
//import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
//import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
//import com.example.portfolio.webstorespring.model.entity.accounts.Account;
//import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.AccountConfToken;
//import com.example.portfolio.webstorespring.repositories.tokens.confirmations.AccountConfTokenRepository;
//import com.example.portfolio.webstorespring.services.tokens.confirmations.AccountConfTokenService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.Clock;
//import java.time.LocalDateTime;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.ZONED_DATE_TIME;
//import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.setupClock;
//import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
//import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.AccountConfTokenBuilderHelper.*;
//import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.*;
//import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.EXPIRES_AT;
//import static com.natpryce.makeiteasy.MakeItEasy.*;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AccountConfTokenServiceTest {
//
//    @Mock
//    private AccountConfTokenRepository tokenRepository;
//    @Mock
//    private Clock clock;
//    @InjectMocks
//    private AccountConfTokenService underTest;
//
//    private static final String UUID_CODE = UUID.randomUUID().toString();
//
//    @Test
//    void shouldCreateConfirmationToken() {
//        createConfirmationTokenTest(account -> underTest.create(account));
//    }
//
//    @Test
//    void shouldGetConfirmationTokenByToken() {
//        Account account = make(a(BASIC_ACCOUNT));
//        AccountConfToken accountConfToken = createAccountConfToken(account, make(a(BASIC_TOKEN_DETAILS)));
//
//        given(tokenRepository.findByTokenDetails_Token(anyString())).willReturn(Optional.of(accountConfToken));
//
//        AccountConfToken actual = underTest.getByToken(UUID_CODE);
//
//        assertNotNull(actual);
//        verify(tokenRepository, times(1)).findByTokenDetails_Token(UUID_CODE);
//    }
//
//    @Test
//    void willThrowResourceNotFoundException_whenConfirmationTokenNotFound() {
//        given(tokenRepository.findByTokenDetails_Token(anyString())).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> underTest.getByToken(UUID_CODE))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessageContaining("Confirmation token with token " + UUID_CODE + " not found");
//    }
//
//    @Test
//    void shouldConfirmTokenAndExecuteAccountMethod() {
//        Account account = getAccountAndSetupClock();
//        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
//                .but(with(ACCOUNT, account))
//                .but(withNull(CONFIRMED_AT)));
//        Consumer<Account> accountConsumer = mock(Consumer.class);
//        String successfulMessage = "Operation successful";
//
//        given(tokenRepository.findByTokenDetails_Token(anyString())).willReturn(Optional.of(accountConfToken));
//
//        Map<String, Object> response = underTest.confirmTokenAndExecute(accountConfToken.getTokenDetails().getToken(), accountConsumer, successfulMessage);
//
//        assertEquals(successfulMessage, response.get("message"));
//        assertNotNull(accountConfToken.getTokenDetails().getConfirmedAt());
//        verify(tokenRepository, times(1)).findByTokenDetails_Token(anyString());
//        verify(accountConsumer, times(1)).accept(account);
//    }
//
//    @Test
//    void willThrowWhenConfirmationTokenIsConfirmed() {
//        Account account = make(a(BASIC_ACCOUNT));
//        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
//                .but(with(ACCOUNT, account)));
//        given(tokenRepository.findByTokenDetails_Token(anyString())).willReturn(Optional.of(accountConfToken));
//
//        assertThrows(TokenConfirmedException.class, () -> underTest.confirmTokenAndExecute(accountConfToken.getTokenDetails().getToken(), null, ""));
//    }
//
//    @Test
//    void willThrowWhenConfirmationTokenIsExpired() {
//        Account account = getAccountAndSetupClock();
//        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
//                .but(with(ACCOUNT, account))
//                .but(withNull(CONFIRMED_AT))
//                .but(with(EXPIRED_AT, ZONED_DATE_TIME.toLocalDateTime().minusMinutes(10))));
//        given(tokenRepository.findByTokenDetails_Token(anyString())).willReturn(Optional.of(accountConfToken));
//
//        assertThrows(TokenExpiredException.class, () -> underTest.confirmTokenAndExecute(accountConfToken.getTokenDetails().getToken(), null, ""));
//    }
//
//    @Test
//    void shouldSuccess_whenTokenIsNotExpired() {
//        Account account = getAccountAndSetupClock();
//        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
//                .but(with(ACCOUNT, account)));
//
//        boolean isExpired = underTest.isTokenExpired(accountConfToken);
//
//        assertFalse(isExpired);
//    }
//
//    @Test
//    void shouldFail_whenTokenIsExpired() {
//        setupClockWithExpiredTime();
//
//        Account account = make(a(BASIC_ACCOUNT));
//        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
//                .but(with(ACCOUNT, account))
//                .but(with(EXPIRED_AT, DateForTestBuilderHelper.LOCAL_DATE_TIME)));
//
//        boolean isExpired = underTest.isTokenExpired(accountConfToken);
//
//        assertTrue(isExpired);
//    }
//
//    @Test
//    void shouldSetConfirmedAtAndSaveConfirmationToken() {
//        Account account = getAccountAndSetupClock();
//        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
//                .but(with(ACCOUNT, account))
//                .but(withNull(CONFIRMED_AT)));
//
//        underTest.setConfirmedAt(accountConfToken);
//
//        assertNotNull(accountConfToken.getTokenDetails().getConfirmedAt());
//        verify(tokenRepository, times(1)).save(any(AccountConfToken.class));
//    }
//
//    @Test
//    void shouldDeleteToken() {
//        Account account = make(a(BASIC_ACCOUNT));
//        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
//                .but(with(ACCOUNT, account)));
//
//        underTest.delete(accountConfToken);
//
//        verify(tokenRepository, times(1)).delete(accountConfToken);
//    }
//
//    private Account getAccountAndSetupClock() {
//        setupClock(clock);
//        return make(a(BASIC_ACCOUNT));
//    }
//
//    private void createConfirmationTokenTest(Function<Account, AccountConfToken> function) {
//        Account account = getAccountAndSetupClock();
//        AccountConfToken accountConfToken = make(a(BASIC_CONFIRMATION_TOKEN)
//                .but(with(ACCOUNT, account))
//                .but(withNull(CONFIRMED_AT)));
//
//        given(tokenRepository.save(any(AccountConfToken.class))).willReturn(accountConfToken);
//
//        AccountConfToken actual = function.apply(account);
//
//        verify(tokenRepository, times(1)).save(any(AccountConfToken.class));
//
//        assertNotNull(actual);
//        //assertNotNull(actual.getId());
//        assertNull(actual.getTokenDetails().getConfirmedAt());
//        assertEquals(accountConfToken.getTokenDetails().getToken(), actual.getTokenDetails().getToken());
//        assertEquals(accountConfToken.getTokenDetails().getCreatedAt(), actual.getTokenDetails().getCreatedAt());
//        assertEquals(accountConfToken.getTokenDetails().getExpiresAt(), actual.getTokenDetails().getExpiresAt());
//    }
//
//
//    private void setupClockWithExpiredTime() {
//        when(clock.getZone()).thenReturn(ZONED_DATE_TIME.getZone());
//        when(clock.instant()).thenReturn(ZONED_DATE_TIME.plusMinutes(16).toInstant());
//    }
//}
