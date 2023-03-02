package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.porfolio.webstorespring.repositories.AccountAddressRepository;
import com.example.porfolio.webstorespring.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountAddressService extends AuthorityService {

    private final AccountAddressRepository addressRepository;
    private final AccountAddressMapper addressMapper;
    private final AccountRepository accountRepository;

    public AccountAddressDto getAccountAddressByAccountId(Long accountId) {
        Account foundAccount = findAccountById(accountId);

        validateAuthorityLoggedUser(foundAccount, "get");

        return addressMapper.mapToDto(findAccountAddressByAccountId(accountId));
    }

    public AccountAddressDto saveAccountAddress(Long accountId, AccountAddressDto accountAddressDto) {
        Account foundAccount = findAccountById(accountId);

        validateAuthorityLoggedUser(foundAccount, "add");

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressDto);

        accountAddress.setAccount(foundAccount);
        addressRepository.save(accountAddress);
        return addressMapper.mapToDto(accountAddress);
    }

    public AccountAddressDto updateAccountAddress(Long accountId, AccountAddressDto accountAddressDto) {
        Account foundAccount = findAccountById(accountId);
        AccountAddress foundAddress = findAccountAddressByAccountId(accountId);

        validateAuthorityLoggedUser(foundAccount, "update");

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressDto);

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

        if (accountAddress.getAddress() == null) {
            accountAddress.setAddress(foundAddress.getAddress());
        }
        if (accountAddress.getCity() == null) {
            accountAddress.setCity(foundAddress.getCity());
        }
        if (accountAddress.getPostcode() == null) {
            accountAddress.setPostcode(foundAddress.getPostcode());
        }
    }
}
