package com.example.portfolio.webstorespring.services.accounts.aspects;

import com.example.portfolio.webstorespring.exceptions.EmailAlreadyUsedException;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.UpdateEmailRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.UpdatePasswordRequest;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
import com.example.portfolio.webstorespring.services.emails.accountactions.RestoreEmailService;
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
class AccountUpdateAspect {

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
        restoreEmailService.sendRestoreEmail(accountDetails.getAccount());
    }

    @Before(value = "@annotation(com.example.portfolio.webstorespring.annotations.ValidatePasswordUpdate) " +
                    "&& args(accountDetails, updatePasswordRequest)",
            argNames = "accountDetails, updatePasswordRequest")
    public void beforeValidatePasswordUpdate(AccountDetails accountDetails, UpdatePasswordRequest updatePasswordRequest){
        log.info("Validating users passwords");
        validatePassword(updatePasswordRequest.enteredPassword(), accountDetails.getPassword());
    }

    private void validateUniqueNewEmail(String email) {
        log.debug("Validating unique email: {}", email);
        if (Boolean.TRUE.equals(accountRepository.existsByEmail(email))) {
            log.warn("Email already exists: {}", email);
            throw new EmailAlreadyUsedException(email);
        }
    }

    private void validateEmail(String enteredEmail, String currentlyLoggedEmail) {
        log.debug("Validating email: {}", enteredEmail);
        if (!enteredEmail.equals(currentlyLoggedEmail)) {
            log.warn("Invalid email: {}", enteredEmail);
            throw new BadCredentialsException("Email mismatch.");
        }
    }

    private void validatePassword(String rawPassword, String hashPassword) {
        log.debug("Validating entered password.");
        if (!passwordEncoder.matches(rawPassword, hashPassword)) {
            log.warn("Invalid entered password.");
            throw new BadCredentialsException("Password mismatch.");
        }
    }
}
