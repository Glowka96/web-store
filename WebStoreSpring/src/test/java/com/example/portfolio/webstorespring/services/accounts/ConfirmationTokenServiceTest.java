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

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationTokenIsConfirmedAt;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationTokenIsNotConfirmedAt;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        // given
        setupClock();

        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = createConfirmationTokenIsNotConfirmedAt(account);

        given(tokenRepository.save(any(ConfirmationToken.class))).willReturn(confirmationToken);

        // when
        ConfirmationToken actual = underTest.createConfirmationToken(account);

        ArgumentCaptor<ConfirmationToken> confirmationTokenArgumentCaptor =
                ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(tokenRepository).save(confirmationTokenArgumentCaptor.capture());

        ConfirmationToken captureToken = confirmationTokenArgumentCaptor.getValue();

        // then
        assertThat(actual).isNotNull();
        assertThat(captureToken).isEqualTo(actual);
        verify(tokenRepository, times(1)).save(any(ConfirmationToken.class));
    }

    @Test
    void shouldGetConfirmationTokenByToken() {
        // given
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = createConfirmationTokenIsConfirmedAt(account);
        given(tokenRepository.findByToken(anyString())).willReturn(Optional.of(confirmationToken));

        // when
        ConfirmationToken actual = underTest.getConfirmationTokenByToken(UUID_CODE);

        // then
        assertThat(actual).isNotNull();
        verify(tokenRepository, times(1)).findByToken(UUID_CODE);
    }

    @Test
    void willThrowWhenConfirmationTokenNotFound() {
        // given
        given(tokenRepository.findByToken(anyString())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> underTest.getConfirmationTokenByToken(UUID_CODE))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Confirmation token with token " + UUID_CODE + " not found");
    }

    @Test
    void shouldSuccessTokenExpired() {
        // given
        setupClock();

        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = createConfirmationTokenIsConfirmedAt(account);

        // when
        boolean isExpired = underTest.isTokenExpired(confirmationToken);

        // then
        assertThat(isExpired).isFalse();
    }

    @Test
    void shouldFailTokenExpired() {
        // given
        setupClockWithExpiredTime();

        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = createConfirmationTokenIsNotConfirmedAt(account);

        // when
        boolean isExpired = underTest.isTokenExpired(confirmationToken);

        // then
        assertThat(isExpired).isTrue();
    }

    @Test
    void shouldSetConfirmedAtAndSaveConfirmationToken() {
        // given
        setupClock();

        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = createConfirmationTokenIsConfirmedAt(account);

        // when
        underTest.setConfirmedAtAndSaveConfirmationToken(confirmationToken);

        // then
        verify(tokenRepository, times(1)).save(confirmationToken);
        assertThat(confirmationToken.getConfirmedAt()).isNotNull();
    }

    @Test
    void shouldDeleteToken() {
        // given
        Account account = make(a(BASIC_ACCOUNT));
        ConfirmationToken confirmationToken = createConfirmationTokenIsConfirmedAt(account);

        // when
        underTest.deleteConfirmationToken(confirmationToken);

        // then
        verify(tokenRepository, times(1)).delete(confirmationToken);
    }


    private void setupClock() {
        when(clock.getZone()).thenReturn(DateForTestBuilderHelper.ZONED_DATE_TIME.getZone());
        when(clock.instant()).thenReturn(DateForTestBuilderHelper.ZONED_DATE_TIME.toInstant());
    }

    private void setupClockWithExpiredTime() {
        when(clock.getZone()).thenReturn(DateForTestBuilderHelper.ZONED_DATE_TIME.getZone());
        when(clock.instant()).thenReturn(DateForTestBuilderHelper.ZONED_DATE_TIME.plusMinutes(16).toInstant());
    }

}
