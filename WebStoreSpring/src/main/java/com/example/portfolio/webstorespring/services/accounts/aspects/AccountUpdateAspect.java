package com.example.portfolio.webstorespring.services.accounts.aspects;

import com.example.portfolio.webstorespring.exceptions.EmailAlreadyUsedException;
import com.example.portfolio.webstorespring.model.dto.accounts.request.UpdateEmailRequest;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import com.example.portfolio.webstorespring.services.email.RestoreEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AccountUpdateAspect {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RestoreEmailService restoreEmailService;

    @Pointcut(value = "@annotation(com.example.portfolio.webstorespring.annotations.ValidateEmailUpdate) " +
                      "&& args(accountDetails, updateEmailRequest)",
            argNames = "accountDetails,updateEmailRequest")
    public void validateEmailUpdatePointcut(AccountDetails accountDetails,
                                            UpdateEmailRequest updateEmailRequest) {
    }

    @Before(value = "validateEmailUpdatePointcut(accountDetails, updateEmailRequest)",
            argNames = "accountDetails,updateEmailRequest")
    public void beforeValidateEmailUpdate(AccountDetails accountDetails,
                                          UpdateEmailRequest updateEmailRequest) {
        log.info("Starting data validation for email update: {}", updateEmailRequest.email());
        validateUniqueNewEmail(updateEmailRequest.email());
        validateEmail(updateEmailRequest.loginRequest().email(), accountDetails.getUsername());
        validatePassword(updateEmailRequest.loginRequest().password(), accountDetails.getPassword());
        log.info("Validation passed");
    }

    @After(value = "validateEmailUpdatePointcut(accountDetails, updateEmailRequest)",
            argNames = "accountDetails,updateEmailRequest")
    public void afterValidateEmailUpdate(AccountDetails accountDetails,
                                         UpdateEmailRequest updateEmailRequest) {
        restoreEmailService.sendBackupEmail(accountDetails.getAccount());
    }

    private void validateUniqueNewEmail(String email) {
        if (Boolean.TRUE.equals(accountRepository.existsByEmail(email))) {
            throw new EmailAlreadyUsedException();
        }
    }

    private void validateEmail(String enteredEmail, String currentlyLoggedEmail) {
        if (!enteredEmail.equals(currentlyLoggedEmail)) {
            throw new BadCredentialsException("Email mismatch.");
        }
    }

    private void validatePassword(String rawPassword, String hashPassword) {
        if (!passwordEncoder.matches(rawPassword, hashPassword)) {
            throw new BadCredentialsException("Password mismatch.");
        }
    }
}
