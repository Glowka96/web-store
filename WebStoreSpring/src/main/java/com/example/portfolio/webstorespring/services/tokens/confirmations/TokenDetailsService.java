package com.example.portfolio.webstorespring.services.tokens.confirmations;

import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.entity.tokens.confirmations.TokenDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenDetailsService {

    private final Clock clock;

    TokenDetails create(Long expiresMinute) {
        log.info("Creating new token details, with expires: {}", expiresMinute);
        return TokenDetails.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now(clock))
                .expiresAt(LocalDateTime.now(clock).plusMinutes(expiresMinute))
                .build();
    }

    void validateAndConfirm(TokenDetails tokenDetails) {
        validate(tokenDetails);
        setConfirmedAt(tokenDetails);
    }

    private void validate(TokenDetails tokenDetails) {
        log.debug("Validating token: {}.", tokenDetails.getToken());
        if (tokenDetails.getConfirmedAt() != null) {
            log.debug("Invalid - token is confirmed.");
            throw new TokenConfirmedException();
        }

        if (isTokenExpired(tokenDetails)) {
            log.debug("Invalid token is expired");
            throw new TokenExpiredException();
        }
        log.debug("Valid successful.");
    }

    public void setConfirmedAt(TokenDetails token) {
        log.debug("Confirming token: {}.", token.getToken());
        token.setConfirmedAt(LocalDateTime.now(clock));
    }

    public boolean isTokenExpired(TokenDetails token) {
        log.debug("Validating if confirmation token has expired.");
        return token.getExpiresAt().isBefore(LocalDateTime.now(clock));
    }
}
