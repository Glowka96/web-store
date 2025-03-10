package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.models.entities.tokens.confirmations.TokenDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.LOCAL_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.DateForTestBuilderHelper.ZONED_DATE_TIME;
import static com.example.portfolio.webstorespring.buildhelpers.tokens.confirmations.TokenDetailsBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenDetailsServiceTest {

    @Mock
    private Clock clock;
    @InjectMocks
    private TokenDetailsService underTest;

    @Test
    void shouldCreate() {
        setupClock();

        TokenDetails tokenDetails = underTest.create(10_800L);

        assertNotNull(tokenDetails);
    }

    @Test
    void shouldValidate_whenNotConfirmedAndNoExpired() {
        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS)
                .but(withNull(CONFIRMED_AT)));
        setupClock();

        assertDoesNotThrow(() -> underTest.validate(tokenDetails));
    }

    @Test
    void willThrowTokenConfirmedException_whenTokenIsConfirmed() {
        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS));

        assertThrows(TokenConfirmedException.class, () -> underTest.validate(tokenDetails));
    }

    @Test
    void willThrowTokenExpiredException_whenTokenIsExpired() {
        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS)
                .but(withNull(CONFIRMED_AT))
                .but(with(EXPIRES_AT, LOCAL_DATE_TIME.minusMinutes(1))));
        setupClock();

        assertThrows(TokenExpiredException.class, () -> underTest.validate(tokenDetails));
    }

    @Test
    void shouldSetConfirmedAt() {
        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS)
                .but(withNull(CONFIRMED_AT)));
        setupClock();

        underTest.setConfirmedAt(tokenDetails);

        assertNotNull(tokenDetails.getConfirmedAt());
        assertEquals(tokenDetails.getConfirmedAt(), LOCAL_DATE_TIME);
    }

    @Test
    void shouldValidateTokenExpired() {
        TokenDetails tokenDetails = make(a(BASIC_TOKEN_DETAILS));
        setupClock();

        boolean result = underTest.isTokenExpired(tokenDetails);

        assertFalse(result);
    }

    private void setupClock() {
        when(clock.getZone()).thenReturn(ZONED_DATE_TIME.getZone());
        when(clock.instant()).thenReturn(ZONED_DATE_TIME.toInstant());
    }
}