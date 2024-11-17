package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.config.providers.AccountImageUrlProvider;
import com.example.portfolio.webstorespring.config.providers.AdminCredentialsProvider;
import com.example.portfolio.webstorespring.enums.RoleType;
import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final AccountAddressService addressService;
    private final AdminCredentialsProvider adminCredentialsProvider;
    private final AccountImageUrlProvider accountImageUrlProvider;

    public AccountResponse getByAccountDetails(AccountDetails accountDetails) {
        return accountMapper.mapToDto(accountDetails.getAccount());
    }

    @Transactional
    public Account save(RegistrationRequest registrationRequest) {
        return accountRepository.save(Account.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(encoder.encode(registrationRequest.getPassword()))
                .roles(roleService.findByName(RoleType.ROLE_USER.name()))
                .enabled(Boolean.FALSE)
                .imageUrl(accountImageUrlProvider.getUrl())
                .build()
        );
    }

    @Transactional
    public AccountResponse update(AccountDetails accountDetails, AccountRequest accountRequest) {
        Account loggedAccount = accountDetails.getAccount();

        Account updatedAccount = accountMapper.mapToEntity(accountRequest);
        loggedAccount.setFirstName(updatedAccount.getFirstName());
        loggedAccount.setLastName(updatedAccount.getLastName());
        loggedAccount.setPassword(encoder.encode(updatedAccount.getPassword()));
        loggedAccount.setImageUrl(updatedAccount.getImageUrl());

        accountRepository.save(loggedAccount);
        return accountMapper.mapToDto(loggedAccount);
    }

    void initializeAdminAccount(){
        if(Boolean.FALSE.equals(accountRepository.existsByEmail(adminCredentialsProvider.getEmail()))){
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
        accountRepository.delete(accountDetails.getAccount());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setEnabledAccount(Account account) {
        account.setEnabled(true);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setNewAccountPassword(Account account, String password) {
        account.setPassword(encoder.encode(password));
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account with email: " + email + " not found"));
    }
}
