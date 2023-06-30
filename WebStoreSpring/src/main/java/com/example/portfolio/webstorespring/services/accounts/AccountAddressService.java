package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountAddressService{

    private final AccountAddressRepository addressRepository;
    private final AccountAddressMapper addressMapper;
    private final AccountRepository accountRepository;

    public AccountAddressResponse getAccountAddressByAccountId(Long accountId) {
        AccountAddress foundAddress = findAccountAddressByAccountId(accountId);

        return addressMapper.mapToDto(foundAddress);
    }

    public AccountAddressResponse saveAccountAddress(Long accountId, AccountAddressRequest accountAddressRequest) {
        Account foundAccount = findAccountById(accountId);

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressRequest);

        accountAddress.setAccount(foundAccount);
        addressRepository.save(accountAddress);
        return addressMapper.mapToDto(accountAddress);
    }

    public AccountAddressResponse updateAccountAddress(Long accountId, AccountAddressRequest accountAddressRequest) {
        AccountAddress foundAddress = findAccountAddressByAccountId(accountId);

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressRequest);

        setupUpdateAddress(foundAddress, accountAddress);
        addressRepository.save(accountAddress);
        return addressMapper.mapToDto(accountAddress);
    }

    private AccountAddress findAccountAddressByAccountId(Long accountId) {
        return addressRepository.findAccountAddressByAccount_Id(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "account id", accountId));
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));
    }

    private void setupUpdateAddress(AccountAddress foundAddress, AccountAddress accountAddress) {
        accountAddress.setAccount(foundAddress.getAccount());

        if (accountAddress.getStreet() == null) {
            accountAddress.setStreet(foundAddress.getStreet());
        }
        if (accountAddress.getCity() == null) {
            accountAddress.setCity(foundAddress.getCity());
        }
        if (accountAddress.getPostcode() == null) {
            accountAddress.setPostcode(foundAddress.getPostcode());
        }
    }
}
