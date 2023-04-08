package com.example.porfolio.webstorespring.services.accounts;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.porfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountAddressService{

    private final AccountAddressRepository addressRepository;
    private final AccountAddressMapper addressMapper;
    private final AccountRepository accountRepository;

    public AccountAddressDto getAccountAddressByAccountId(Long accountId) {
        AccountAddress foundAddress = findAccountAddressByAccountId(accountId);

        return addressMapper.mapToDto(foundAddress);
    }

    public AccountAddressDto saveAccountAddress(Long accountId, AccountAddressDto accountAddressDto) {
        Account foundAccount = findAccountById(accountId);

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressDto);

        accountAddress.setAccount(foundAccount);
        addressRepository.save(accountAddress);
        return addressMapper.mapToDto(accountAddress);
    }

    public AccountAddressDto updateAccountAddress(Long accountId, AccountAddressDto accountAddressDto) {
        AccountAddress foundAddress = findAccountAddressByAccountId(accountId);

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
