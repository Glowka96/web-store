package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.dto.accounts.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final BCryptPasswordEncoder encoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderConfiguration emailSenderConfiguration;
    private final AccountRepository accountRepository;

    public Map<String, Object> registrationAccount(RegistrationRequest registrationRequest) {
        Account account = setupNewAccount(registrationRequest);
        accountRepository.save(account);

        ConfirmationToken savedToken = confirmationTokenService.createConfirmationToken(account);
        return emailSenderConfiguration.sendEmail(account.getEmail(),
                "Complete Registration!",
                savedToken.getToken());
    }

    public Map<String, Object> confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token);
        Account account = confirmationToken.getAccount();

        if (confirmationTokenService.isConfirmed(confirmationToken)) {
            throw new EmailAlreadyConfirmedException();
        }

        if (!account.getEnabled() && confirmationTokenService.isTokenExpired(confirmationToken)) {
            ConfirmationToken newToken = confirmationTokenService.createConfirmationToken(account);
            confirmationTokenService.deleteConfirmationToken(confirmationToken);
            return emailSenderConfiguration.sendEmail(account.getEmail(),
                    "New confirmation token",
                    newToken.getToken());
        }

        confirmationTokenService.setConfirmedAtAndSaveConfirmationToken(confirmationToken);
        account.setEnabled(true);
        accountRepository.save(account);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Account confirmed");
        return response;
    }

    private Account setupNewAccount(RegistrationRequest registrationRequest) {
        Account account = new Account();
        account.setFirstName(registrationRequest.getFirstName());
        account.setLastName(registrationRequest.getLastName());
        account.setEmail(registrationRequest.getEmail());
        account.setPassword(encoder.encode(registrationRequest.getPassword()));
        account.setAccountRoles(AccountRoles.ROLE_USER);
        account.setEnabled(false);
        account.setImageUrl("https://i.imgur.com/a23SANX.png");
        return account;
    }
}
