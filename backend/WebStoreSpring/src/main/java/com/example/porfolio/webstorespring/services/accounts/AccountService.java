package com.example.porfolio.webstorespring.services.accounts;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.AccountMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
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

    public AccountDto updateAccount(Long accountId, AccountDto accountDto) {
        Account foundAccount = findAccountById(accountId);

        Account updatedAccount = accountMapper.mapToEntity(accountDto);
        setupUpdateAccount(foundAccount, updatedAccount);

        accountRepository.save(updatedAccount);
        return accountMapper.mapToDto(updatedAccount);
    }

    public void deleteAccountById(Long accountId) {
        Account foundAccount = findAccountById(accountId);

        accountRepository.delete(foundAccount);
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));
    }

    private void setupUpdateAccount(Account foundAccount, Account updatedAccount) {
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
        if (updatedAccount.getAccountRoles() == null) {
            updatedAccount.setAccountRoles(foundAccount.getAccountRoles());
        }
    }
}
