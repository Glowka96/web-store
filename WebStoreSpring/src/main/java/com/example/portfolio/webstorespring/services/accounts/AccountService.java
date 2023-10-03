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
        AccountDetails accountDetails = getAccountDetails();
        return accountMapper.mapToDto(accountDetails.getAccount());
    }

    public AccountResponse updateAccount(AccountRequest accountRequest) {
        Account account = getAccountDetails().getAccount();

        Account updatedAccount = accountMapper.mapToEntity(accountRequest);
        setupAccount(account, updatedAccount);

        accountRepository.save(account);
        return accountMapper.mapToDto(updatedAccount);
    }

    public void deleteAccount() {
        Account foundAccount = getAccountDetails().getAccount();
        accountRepository.delete(foundAccount);
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
