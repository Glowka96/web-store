package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationToken;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.ConfirmationTokenBuilderHelper.createConfirmationTokenIsNotConfirmedAt;
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

    private final ZonedDateTime zonedDateTime = ZonedDateTime.of(
            2023,
            3,
            9,
            12,
            30,
            30,
            0,
            ZoneId.of("GMT")
    );

    private static final String UUID_CODE = UUID.randomUUID().toString();

    @Test
    void shouldCreateConfirmationToken() {
        // given
        setupClockWhatItReturend();

        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account, clock);

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
        setupClockWhatItReturend();

        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account, clock);
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
    void shouldSuccessConfirmed() {
        // given
        setupClockWhatItReturend();

        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account, clock);

        // when
        boolean isConfirmed = underTest.isConfirmed(confirmationToken);

        // then
        assertThat(isConfirmed).isTrue();
    }

    @Test
    void shouldFailConfirmed() {
        // given
        setupClockWhatItReturend();

        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationTokenIsNotConfirmedAt(account, clock);

        // when
        boolean isNotConfirmed = underTest.isConfirmed(confirmationToken);

        // then
        assertThat(isNotConfirmed).isFalse();
    }

    @Test
    void shouldSuccessTokenExpired() {
        // given
        setupClockWhatItReturend();

        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account, clock);

        // when
        boolean isExpired = underTest.isTokenExpired(confirmationToken);

        // then
        assertThat(isExpired).isTrue();
    }

    @Test
    void shouldFailTokenExpired() {
        // given
        setupClockWhatItReturend();

        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account, clock);
        confirmationToken.setExpiresAt(LocalDateTime.now(clock).plusMinutes(15));

        // when
        boolean isExpired = underTest.isTokenExpired(confirmationToken);

        // then
        assertThat(isExpired).isTrue();
    }

    @Test
    void shouldSetConfirmedAtAndSaveConfirmationToken() {
        // given
        setupClockWhatItReturend();

        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account, clock);

        // when
        underTest.setConfirmedAtAndSaveConfirmationToken(confirmationToken);

        // then
        verify(tokenRepository, times(1)).save(confirmationToken);
        assertThat(confirmationToken.getConfirmedAt()).isNotNull();
    }

    @Test
    void shouldDeleteToken() {
        // given
        setupClockWhatItReturend();

        Account account = AccountBuilderHelper.createAccountWithRoleUser();
        ConfirmationToken confirmationToken = createConfirmationToken(account, clock);

        // when
        underTest.deleteConfirmationToken(confirmationToken);

        // then
        verify(tokenRepository, times(1)).delete(confirmationToken);
    }


    private void setupClockWhatItReturend() {
        when(clock.getZone()).thenReturn(zonedDateTime.getZone());
        when(clock.instant()).thenReturn(zonedDateTime.toInstant());
    }

}
