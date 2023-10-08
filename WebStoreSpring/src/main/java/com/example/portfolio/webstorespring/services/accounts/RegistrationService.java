package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.EmailAlreadyConfirmedException;
import com.example.portfolio.webstorespring.model.dto.accounts.RegistrationRequest;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import com.example.portfolio.webstorespring.services.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RegistrationService {

    private final BCryptPasswordEncoder encoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    private static final String ROLE_USER = "ROLE_USER";

    @Value("${email.confirmation.link}")
    private String confirmLink;

    @Autowired
    public RegistrationService(BCryptPasswordEncoder encoder,
                               ConfirmationTokenService confirmationTokenService,
                               @Qualifier(value = "confirmEmailSender") EmailSenderService emailSenderService,
                               AccountRepository accountRepository, RoleRepository roleRepository) {
        this.encoder = encoder;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    public Map<String, Object> registrationAccount(RegistrationRequest registrationRequest) {
        Account account = setupNewAccount(registrationRequest);
        accountRepository.save(account);

        ConfirmationToken savedToken = confirmationTokenService.createConfirmationToken(account);
        return emailSenderService.sendEmail(account.getEmail(),
                confirmLink + savedToken.getToken());
    }

    public Map<String, Object> confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token);
        Account account = confirmationToken.getAccount();

        if (confirmationTokenService.isConfirmed(confirmationToken)) {
            throw new EmailAlreadyConfirmedException();
        }

        if (Boolean.TRUE.equals(!account.getEnabled()) && confirmationTokenService.isTokenExpired(confirmationToken)) {
            ConfirmationToken newToken = confirmationTokenService.createConfirmationToken(account);
            confirmationTokenService.deleteConfirmationToken(confirmationToken);
            return emailSenderService.sendEmail(account.getEmail(),
                    confirmLink + newToken.getToken());
        }

        confirmationTokenService.setConfirmedAtAndSaveConfirmationToken(confirmationToken);
        account.setEnabled(true);
        account.setRoles(roleRepository.findByName(ROLE_USER));
        accountRepository.save(account);

        return Map.of("message", "Account confirmed");
    }

    private Account setupNewAccount(RegistrationRequest registrationRequest) {
        return Account.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(encoder.encode(registrationRequest.getPassword()))
                .roles(roleRepository.findByName(ROLE_USER))
                .enabled(false)
                .imageUrl("https://i.imgur.com/a23SANX.png")
                .build();
    }
}
