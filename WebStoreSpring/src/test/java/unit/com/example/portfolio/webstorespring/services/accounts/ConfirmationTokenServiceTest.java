package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.ConfirmationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.ZONED_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.setupClock;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {

    @Mock
    private ConfirmationTokenRepository tokenRepository;
    @Mock
    private Clock clock;
    @InjectMocks
    private ConfirmationTokenService underTest;

    private static final String UUID_CODE = UUID.randomUUID().toString();

    @Test
    void shouldCreateConfirmationToken() {
        createConfirmationTokenTest(account -> underTest.create(account));
    }

    @Test
    void shouldCreateConfirmationTokenWith7DaysExpires() {
        createConfirmationTokenTest(account -> underTest.createWith7DaysExpires(account));
    }

    @Test
    void shouldGetConfirmationTokenByToken() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));
        given(tokenRepository.findByToken(anyString())).willReturn(Optional.of(confirmationToken));

        ConfirmationToken actual = underTest.getByToken(UUID_CODE);

        assertNotNull(actual);
        verify(tokenRepository, times(1)).findByToken(UUID_CODE);
    }

    @Test
    void willThrowResourceNotFoundException_whenConfirmationTokenNotFound() {
        given(tokenRepository.findByToken(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getByToken(UUID_CODE))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Confirmation token with token " + UUID_CODE + " not found");
    }

    @Test
    void shouldConfirmTokenAndExecuteAccountMethod() {
        Account account = getAccountAndSetupClock();
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));
        Consumer<Account> accountConsumer = mock(Consumer.class);
        String successfulMessage = "Operation successful";

        given(tokenRepository.findByToken(anyString())).willReturn(Optional.of(confirmationToken));

        Map<String, Object> response = underTest.confirmTokenAndExecute(confirmationToken.getToken(), accountConsumer, successfulMessage);

        assertEquals(successfulMessage, response.get("message"));
        assertNotNull(confirmationToken.getConfirmedAt());
        verify(tokenRepository, times(1)).findByToken(anyString());
        verify(accountConsumer, times(1)).accept(account);
    }

    @Test
    void willThrowWhenConfirmationTokenIsConfirmed() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));
        given(tokenRepository.findByToken(anyString())).willReturn(Optional.of(confirmationToken));

        assertThrows(TokenConfirmedException.class, () -> underTest.confirmTokenAndExecute(confirmationToken.getToken(), null, ""));
    }

    @Test
    void willThrowWhenConfirmationTokenIsExpired() {
        Account account = getAccountAndSetupClock();
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT))
                .but(with(EXPIRED_AT, ZONED_DATE_TIME.toLocalDateTime().minusMinutes(10))));
        given(tokenRepository.findByToken(anyString())).willReturn(Optional.of(confirmationToken));

        assertThrows(TokenExpiredException.class, () -> underTest.confirmTokenAndExecute(confirmationToken.getToken(), null, ""));
    }

    @Test
    void shouldSuccess_whenTokenIsNotExpired() {
        Account account = getAccountAndSetupClock();
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));

        boolean isExpired = underTest.isTokenExpired(confirmationToken);

        assertFalse(isExpired);
    }

    @Test
    void shouldFail_whenTokenIsExpired() {
        setupClockWithExpiredTime();

        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(with(EXPIRED_AT, DateForTestBuilderHelper.LOCAL_DATE_TIME)));

        boolean isExpired = underTest.isTokenExpired(confirmationToken);

        assertTrue(isExpired);
    }

    @Test
    void shouldSetConfirmedAtAndSaveConfirmationToken() {
        Account account = getAccountAndSetupClock();
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));

        underTest.setConfirmedAt(confirmationToken);

        assertNotNull(confirmationToken.getConfirmedAt());
        verify(tokenRepository, times(1)).save(any(ConfirmationToken.class));
    }

    @Test
    void shouldDeleteToken() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));

        underTest.delete(confirmationToken);

        verify(tokenRepository, times(1)).delete(confirmationToken);
    }

    private Account getAccountAndSetupClock() {
        setupClock(clock);
        return make(a(BASIC_ACCOUNT));
    }

    private void createConfirmationTokenTest(Function<Account, ConfirmationToken> function) {
        Account account = getAccountAndSetupClock();
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));

        given(tokenRepository.save(any(ConfirmationToken.class))).willReturn(confirmationToken);

        ConfirmationToken actual = function.apply(account);

        verify(tokenRepository, times(1)).save(any(ConfirmationToken.class));

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNull(actual.getConfirmedAt());
        assertEquals(confirmationToken.getToken(), actual.getToken());
        assertEquals(confirmationToken.getCreatedAt(), actual.getCreatedAt());
        assertEquals(confirmationToken.getExpiresAt(), actual.getExpiresAt());
    }


    private void setupClockWithExpiredTime() {
        when(clock.getZone()).thenReturn(ZONED_DATE_TIME.getZone());
        when(clock.instant()).thenReturn(ZONED_DATE_TIME.plusMinutes(16).toInstant());
    }
}
