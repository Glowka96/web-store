package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.AccountCanNotModifiedException;
import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.AccountMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.example.porfolio.webstorespring.repositories.AccountRepository;
import com.example.porfolio.webstorespring.services.auth.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BCryptPasswordEncoder encoder;

    public AccountDto getAccountById(Long accountId) {
        Account foundAccount = findAccountById(accountId);
        return accountMapper.mapToDto(foundAccount);
    }

    public AccountDto saveAccount(AccountDto accountDto) {
        Account account = accountMapper.mapToEntity(accountDto);
        account.setPassword(encoder.encode(account.getPassword()));
        accountRepository.save(account);
        return accountMapper.mapToDto(account);
    }

    public AccountDto updateAccount(Long accountId, AccountDto accountDto) {
        Account foundAccount = findAccountById(accountId);

        checkValidAlreadyLoggedUser(foundAccount, "update");

        Account updatedAccount = accountMapper.mapToEntity(accountDto);
        setupAccount(foundAccount, updatedAccount);

        accountRepository.save(updatedAccount);
        return accountMapper.mapToDto(updatedAccount);
    }

    public void deleteAccountById(Long accountId) {
        Account foundAccount = findAccountById(accountId);

        checkValidAlreadyLoggedUser(foundAccount, "delete");

        accountRepository.delete(foundAccount);
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));
    }

    private void checkValidAlreadyLoggedUser(Account foundAccount, String option) {
        AccountDetails principal = getPrincipal();

        String loggedUsername = principal.getUsername();

        String authority = getNameAuthority(principal);

        if (authority != null &&
                authority.equalsIgnoreCase(AccountRoles.USER.name()) &&
                !loggedUsername.equalsIgnoreCase(foundAccount.getEmail())) {
            throw new AccountCanNotModifiedException(option);
        }
    }

    private AccountDetails getPrincipal() {
        return (AccountDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private String getNameAuthority(AccountDetails principal) {
        return principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }

    private void setupAccount(Account foundAccount, Account updatedAccount) {
        updatedAccount.setId(foundAccount.getId());

        if (updatedAccount.getFirstName() == null) {
            updatedAccount.setFirstName(foundAccount.getFirstName());
        }
        if (updatedAccount.getLastName() == null) {
            updatedAccount.setLastName(foundAccount.getLastName());
        }
        if (updatedAccount.getEmail() == null) {
            updatedAccount.setEmail(foundAccount.getEmail());
        }
        if (updatedAccount.getPassword() == null) {
            updatedAccount.setPassword(foundAccount.getPassword());
        } else {
            updatedAccount.setPassword(encoder.encode(updatedAccount.getPassword()));
        }
        if (updatedAccount.getAddress() == null) {
            updatedAccount.setAddress(foundAccount.getAddress());
        }
        if (updatedAccount.getImageUrl() == null) {
            updatedAccount.setImageUrl(foundAccount.getImageUrl());
        }
        if (updatedAccount.getOrders() == null) {
            updatedAccount.setOrders(foundAccount.getOrders());
        }
    }
}
