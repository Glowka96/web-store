package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper;
import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.ConfirmationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

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
        setupClock(clock);

        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));

        given(tokenRepository.save(any(ConfirmationToken.class))).willReturn(confirmationToken);

        ConfirmationToken actual = underTest.createConfirmationToken(account);

        ArgumentCaptor<ConfirmationToken> confirmationTokenArgumentCaptor =
                ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(tokenRepository).save(confirmationTokenArgumentCaptor.capture());

        assertNotNull(actual);
       assertEquals(confirmationTokenArgumentCaptor.getValue(), actual);
        verify(tokenRepository, times(1)).save(any(ConfirmationToken.class));
    }

    @Test
    void shouldGetConfirmationTokenByToken() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));
        given(tokenRepository.findByToken(anyString())).willReturn(Optional.of(confirmationToken));

        ConfirmationToken actual = underTest.getConfirmationTokenByToken(UUID_CODE);

        assertNotNull(actual);
        verify(tokenRepository, times(1)).findByToken(UUID_CODE);
    }

    @Test
    void willThrowResourceNotFoundException_whenConfirmationTokenNotFound() {
        given(tokenRepository.findByToken(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getConfirmationTokenByToken(UUID_CODE))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Confirmation token with token " + UUID_CODE + " not found");
    }

    @Test
    void shouldSuccess_whenTokenIsNotExpired() {
        setupClock(clock);

        Account account = make(a(BASIC_ACCOUNT));
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
        setupClock(clock);

        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account))
                .but(withNull(CONFIRMED_AT)));

        underTest.setConfirmedAtAndSaveConfirmationToken(confirmationToken);

        verify(tokenRepository, times(1)).save(confirmationToken);
        assertNotNull(confirmationToken.getConfirmedAt());
    }

    @Test
    void shouldDeleteToken() {
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = make(a(BASIC_CONFIRMATION_TOKEN)
                .but(with(ACCOUNT, account)));

        underTest.deleteConfirmationToken(confirmationToken);

        verify(tokenRepository, times(1)).delete(confirmationToken);
    }

    private void setupClockWithExpiredTime() {
        when(clock.getZone()).thenReturn(ZONED_DATE_TIME.getZone());
        when(clock.instant()).thenReturn(ZONED_DATE_TIME.plusMinutes(16).toInstant());
    }

}
