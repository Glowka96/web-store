package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
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

    public AccountResponse getAccount() {
        return accountMapper.mapToDto(getAccountDetails().getAccount());
    }

    @Transactional
    public AccountResponse updateAccount(AccountRequest accountRequest) {
        Account loggedAccount = getAccountDetails().getAccount();

        Account updatedAccount = accountMapper.mapToEntity(accountRequest);
        setupAccount(loggedAccount, updatedAccount);

        accountRepository.save(loggedAccount);
        return accountMapper.mapToDto(loggedAccount);
    }

    public void deleteAccount() {
        accountRepository.delete(getAccountDetails().getAccount());
    }

    private AccountDetails getAccountDetails() {
        return (AccountDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void setupAccount(Account loggedAccount, Account updatedAccount) {
        loggedAccount.setFirstName(updatedAccount.getFirstName());
        loggedAccount.setLastName(updatedAccount.getLastName());
        loggedAccount.setPassword(encoder.encode(updatedAccount.getPassword()));
        loggedAccount.setImageUrl(updatedAccount.getImageUrl());
    }
}
