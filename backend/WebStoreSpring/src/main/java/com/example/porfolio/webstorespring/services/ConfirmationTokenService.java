package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.porfolio.webstorespring.repositories.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Clock clock = Clock.systemUTC();

    public ConfirmationToken createConfirmationToken(Account account) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(clock),
                LocalDateTime.now(clock).plusMinutes(15),
                account
        );
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    public ConfirmationToken getConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token","token", token));
    }

    public boolean isConfirmed(ConfirmationToken token) {
        return token.getConfirmedAt() != null;
    }

    public boolean isTokenExpired(ConfirmationToken token) {
        return token.getExpiresAt().isBefore(LocalDateTime.now(clock));
    }

    public void setConfirmedAtAndSaveConfirmationToken(ConfirmationToken token) {
        token.setConfirmedAt(LocalDateTime.now(clock));
        confirmationTokenRepository.save(token);
    }

    public void deleteConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }
}
