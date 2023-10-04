package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.security.auth.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BCryptPasswordEncoder encoder;

    public AccountResponse getAccount() {
        return accountMapper.mapToDto(getAccountDetails().getAccount());
    }

    public AccountResponse updateAccount(AccountRequest accountRequest) {
        Account loggedAccount = getAccountDetails().getAccount();

        Account updatedAccount = accountMapper.mapToEntity(accountRequest);
        setupAccount(loggedAccount, updatedAccount);

        accountRepository.save(loggedAccount);
        return accountMapper.mapToDto(updatedAccount);
    }

    public void deleteAccount() {
        accountRepository.delete(getAccountDetails().getAccount());
    }

    private AccountDetails getAccountDetails() {
        return (AccountDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void setupAccount(Account account, Account updatedAccount) {
        account.setFirstName(updatedAccount.getFirstName());
        account.setLastName(updatedAccount.getLastName());
        account.setPassword(encoder.encode(updatedAccount.getPassword()));
        account.setImageUrl(updatedAccount.getImageUrl());
    }
}
