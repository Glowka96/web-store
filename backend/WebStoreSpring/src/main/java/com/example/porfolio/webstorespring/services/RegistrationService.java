package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.porfolio.webstorespring.model.dto.accounts.RegistrationRequest;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.example.porfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.porfolio.webstorespring.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final BCryptPasswordEncoder encoder;
    private final ConfirmationTokenService tokenService;
    private final EmailSenderService emailSenderService;
    private final AccountRepository accountRepository;

    public String registrationAccount(RegistrationRequest registrationRequest) {
        Account account = setupNewAccount(registrationRequest);
        accountRepository.save(account);

        ConfirmationToken savedToken = tokenService.createConfirmationToken(account);
        return emailSenderService.sendEmail(account.getEmail(),
                "Complete Registration!",
                savedToken.getToken());
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = tokenService.getConfirmationTokenByToken(token);
        Account account = confirmationToken.getAccount();

        if (tokenService.isConfirmed(confirmationToken)) {
            throw new EmailAlreadyConfirmedException();
        }

        if (!account.getEnabled() && tokenService.isTokenExpired(confirmationToken)) {
            ConfirmationToken newToken = tokenService.createConfirmationToken(account);
            tokenService.deleteConfirmationToken(confirmationToken);
            return emailSenderService.sendEmail(account.getEmail(),
                    "New confirmation token",
                    newToken.getToken());
        }

        tokenService.setConfirmedAtAndSaveConfirmationToken(confirmationToken);
        account.setEnabled(true);
        accountRepository.save(account);
        return "Account confirmed";
    }

    private Account setupNewAccount(RegistrationRequest registrationRequest) {
        Account account = new Account();
        account.setFirstName(registrationRequest.getFirstName());
        account.setLastName(registrationRequest.getLastName());
        account.setEmail(registrationRequest.getEmail());
        account.setPassword(encoder.encode(registrationRequest.getPassword()));
        account.setAccountRoles(AccountRoles.USER);
        account.setEnabled(false);
        return account;
    }
}
