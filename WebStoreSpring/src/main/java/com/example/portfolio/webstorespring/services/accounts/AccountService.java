package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.config.providers.AccountImageUrlProvider;
import com.example.portfolio.webstorespring.config.providers.AdminCredentialsProvider;
import com.example.portfolio.webstorespring.enums.RoleType;
import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.UpdatePasswordRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final AccountAddressService addressService;
    private final AdminCredentialsProvider adminCredentialsProvider;
    private final AccountImageUrlProvider accountImageUrlProvider;

    public AccountResponse getByAccountDetails(AccountDetails accountDetails) {
        log.info("Mapping logged account with account ID: {}", accountDetails.getAccount().getId());
        return AccountMapper.mapToDto(accountDetails.getAccount());
    }

    @Transactional
    public Account save(RegistrationRequest registrationRequest) {
        log.info("Saving new account.");
        return accountRepository.save(Account.builder()
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .email(registrationRequest.email())
                .password(encoder.encode(registrationRequest.password()))
                .roles(roleService.findByName(RoleType.ROLE_USER.name()))
                .enabled(Boolean.FALSE)
                .imageUrl(accountImageUrlProvider.getUrl())
                .build()
        );
    }

    @Transactional
    public AccountResponse update(AccountDetails accountDetails, AccountRequest accountRequest) {
        log.info("Fetching logged account with account ID: {}", accountDetails.getAccount().getId());
        Account loggedAccount = accountDetails.getAccount();

        log.debug("Mapping request: {}", accountRequest);
        Account updatedAccount = AccountMapper.mapToEntity(accountRequest);

        log.debug("Setting new data.");
        loggedAccount.setFirstName(updatedAccount.getFirstName());
        loggedAccount.setLastName(updatedAccount.getLastName());
        loggedAccount.setImageUrl(updatedAccount.getImageUrl());

        accountRepository.save(loggedAccount);
        log.info("Updated account with account ID: {}", loggedAccount.getId());
        return AccountMapper.mapToDto(loggedAccount);
    }

    @Transactional
    public Map<String, Object> updatePassword(AccountDetails accountDetails, UpdatePasswordRequest updatePasswordRequest) {
        log.info("Fetching logged account with account ID: {}", accountDetails.getAccount().getId());
        Account loggedAccount = accountDetails.getAccount();

        validatePassword(updatePasswordRequest.enteredPassword(), loggedAccount);

        setNewAccountPassword(loggedAccount, updatePasswordRequest.newPassword());
        log.info("Saved new password for account ID: {}", loggedAccount.getId());
        return Map.of("message", "Password updated successfully.");
    }

    void initializeAdminAccount(){
        log.debug("Checking if admin account exist");
        if(Boolean.FALSE.equals(accountRepository.existsByEmail(adminCredentialsProvider.getEmail()))){
            log.debug("Admin account doesn't exist. Creating new admin account.");
            accountRepository.save(Account.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email(adminCredentialsProvider.getEmail())
                    .password(encoder.encode(adminCredentialsProvider.getPassword()))
                    .roles(roleService.findByName(RoleType.ROLE_ADMIN.name()))
                    .imageUrl(accountImageUrlProvider.getUrl())
                    .enabled(Boolean.TRUE)
                    .build()
            );
        }
    }

    @Transactional
    public void delete(AccountDetails accountDetails) {
        addressService.deleteByAccountDetails(accountDetails);
        log.info("Deleting account for account id: {}", accountDetails.getAccount().getId());
        accountRepository.delete(accountDetails.getAccount());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setEnabledAccount(Account account) {
        log.debug("Enabling account with ID: {}", account.getId());
        account.setEnabled(true);
    }

    public void setNewAccountPassword(Account account, String password) {
        log.debug("Setting password for account ID: {}", account.getId());
        account.setPassword(encoder.encode(password));
        accountRepository.save(account);
    }

    public Account findByEmail(String email) {
        log.debug("Finding account by email: {}", email);
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account with email: " + email + " not found"));
    }

    private void validatePassword(String enteredPassword, Account account) {
        log.debug("Validating entered password.");
        if(!encoder.matches(enteredPassword, account.getPassword())) {
            log.debug("Invalid entered password.");
            throw new BadCredentialsException("Invalid password.");
        }
    }
}
