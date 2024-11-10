package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Clock clock;

    @Transactional
    public ConfirmationToken create(Account account) {
       return new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(clock),
                LocalDateTime.now(clock).plusMinutes(15),
                account
        );
    }

    public ConfirmationToken getByToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation token","token", token));
    }

    public boolean isTokenExpired(ConfirmationToken token) {
        return token.getExpiresAt().isBefore(LocalDateTime.now(clock));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setConfirmedAt(ConfirmationToken token) {
        token.setConfirmedAt(LocalDateTime.now(clock));
    }

    public void delete(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }
}
