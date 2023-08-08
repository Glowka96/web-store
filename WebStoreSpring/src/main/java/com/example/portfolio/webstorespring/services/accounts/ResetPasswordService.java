package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.exceptions.TokenConfirmedException;
import com.example.portfolio.webstorespring.exceptions.TokenExpiredException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor()
public class ResetPasswordService {

    private final BCryptPasswordEncoder encoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Qualifier("resetPasswordSender")
    private final EmailSenderService emailSenderService;
    private final AccountRepository accountRepository;

    public Map<String, Object> resetPasswordByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() ->  new ResourceNotFoundException("Account", "email", email));

        ConfirmationToken savedToken = confirmationTokenService.createConfirmationToken(account);
        return emailSenderService.sendEmail(account.getEmail(),
                savedToken.getToken());
    }

    public Map<String, Object> confirmResetPassword(String password, String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token);
        Account account = confirmationToken.getAccount();

        if (confirmationTokenService.isConfirmed(confirmationToken)) {
            throw new TokenConfirmedException();
        }

        if (confirmationTokenService.isTokenExpired(confirmationToken)) {
            throw new TokenExpiredException();
        }

        confirmationTokenService.setConfirmedAtAndSaveConfirmationToken(confirmationToken);
        account.setPassword(encoder.encode(password));
        accountRepository.save(account);

        return Map.of("message", "Your new password has been saved");
    }
}
