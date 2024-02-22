package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private static final String ROLE_USER = "ROLE_USER";

    @Value("${account.image.url}")
    private String accountImageURL;

    public AccountResponse getAccount() {
        return accountMapper.mapToDto(getAccountDetails().getAccount());
    }

    @Transactional
    public AccountResponse updateAccount(AccountRequest accountRequest) {
        Account loggedAccount = getAccountDetails().getAccount();

        Account updatedAccount = accountMapper.mapToEntity(accountRequest);
        loggedAccount.setFirstName(updatedAccount.getFirstName());
        loggedAccount.setLastName(updatedAccount.getLastName());
        loggedAccount.setPassword(encoder.encode(updatedAccount.getPassword()));
        loggedAccount.setImageUrl(updatedAccount.getImageUrl());

        accountRepository.save(loggedAccount);
        return accountMapper.mapToDto(loggedAccount);
    }

     Account saveAccount(RegistrationRequest registrationRequest) {
        return Account.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(encoder.encode(registrationRequest.getPassword()))
                .roles(roleService.findRoleByName(ROLE_USER))
                .enabled(false)
                .imageUrl(accountImageURL)
                .build();
    }

    void setEnabledAccount(Account account) {
        account.setEnabled(true);
        accountRepository.save(account);
    }

    void setNewAccountPassword(Account account, String password) {
        account.setPassword(encoder.encode(password));
        accountRepository.save(account);
    }

    public void deleteAccount() {
        accountRepository.delete(getAccountDetails().getAccount());
    }

    Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "email", email));
    }

    private AccountDetails getAccountDetails() {
        return (AccountDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
