package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.annotations.ValidateEmailUpdate;
import com.example.portfolio.webstorespring.annotations.ValidatePasswordUpdate;
import com.example.portfolio.webstorespring.configs.providers.AccountImageUrlProvider;
import com.example.portfolio.webstorespring.configs.providers.AdminCredentialsProvider;
import com.example.portfolio.webstorespring.enums.RoleType;
import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.models.dtos.ResponseMessageDTO;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.AccountRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.RegistrationRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.UpdateEmailRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.UpdatePasswordRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AccountResponse;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static final String SUCCESS_UPDATE_EMAIL_RESPONSE_MESSAGE = "Email updated successfully.";
    private static final String SUCCESS_RESET_PASSWORD_RESPONSE_MESSAGE = "Password updated successfully.";

    public AccountResponse getByAccountDetails(AccountDetails accountDetails) {
        log.info("Mapping logged account with account ID: {}", accountDetails.getAccount().getId());
        return AccountMapper.mapToResponse(accountDetails.getAccount());
    }

    @Transactional
    public Account save(RegistrationRequest request) {
        log.info("Saving new account.");
        return accountRepository.save(Account.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .roles(roleService.findByName(RoleType.ROLE_USER.name()))
                .enabled(Boolean.FALSE)
                .imageUrl(accountImageUrlProvider.getUrl())
                .build()
        );
    }

    @Transactional
    public AccountResponse update(AccountDetails accountDetails, AccountRequest request) {
        log.info("Fetching logged account with account ID: {}", accountDetails.getAccount().getId());
        Account loggedAccount = accountDetails.getAccount();

        log.debug("Mapping request: {}", request);
        Account updatedAccount = AccountMapper.mapToEntity(request);

        log.debug("Setting new data.");
        loggedAccount.setFirstName(updatedAccount.getFirstName());
        loggedAccount.setLastName(updatedAccount.getLastName());
        loggedAccount.setImageUrl(updatedAccount.getImageUrl());

        accountRepository.save(loggedAccount);
        log.info("Updated account with account ID: {}", loggedAccount.getId());
        return AccountMapper.mapToResponse(loggedAccount);
    }

    @Transactional
    @ValidateEmailUpdate
    public ResponseMessageDTO updateEmail(AccountDetails accountDetails,
                                          UpdateEmailRequest request) {
        log.info("Update email: {} for account: {}", request, accountDetails.getUsername());
        Account loggedAccount = accountDetails.getAccount();
        loggedAccount.setBackupEmail(loggedAccount.getEmail());
        loggedAccount.setEmail(request.email());
        accountRepository.save(loggedAccount);
        log.info("Updated successful.");
        return new ResponseMessageDTO(SUCCESS_UPDATE_EMAIL_RESPONSE_MESSAGE);
    }

    @Transactional
    @ValidatePasswordUpdate
    public ResponseMessageDTO updatePassword(AccountDetails accountDetails, UpdatePasswordRequest request) {
        log.info("Fetching logged account with account ID: {}", accountDetails.getAccount().getId());
        Account loggedAccount = accountDetails.getAccount();
        setNewAccountPassword(loggedAccount, request.newPassword());
        log.info("Saved new password for account ID: {}", loggedAccount.getId());
        return new ResponseMessageDTO(SUCCESS_RESET_PASSWORD_RESPONSE_MESSAGE);
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

    public void setNewAccountPassword(Account account, String password) {
        log.debug("Setting password for account ID: {}", account.getId());
        account.setPassword(encoder.encode(password));
        accountRepository.save(account);
    }

    @Transactional
    public void restoreEmail(Account account) {
        log.info("Restoring email for account ID: {}", account.getId());
        account.setEmail(account.getBackupEmail());
        account.setBackupEmail(null);
        log.info("Restored email for account ID: {}", account.getId());
    }

    public Account findByEmail(String email) {
        log.debug("Finding account by email: {}", email);
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account with email: " + email + " not found"));
    }
}
