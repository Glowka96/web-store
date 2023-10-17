package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountAddressService {

    private final AccountAddressRepository addressRepository;
    private final AccountAddressMapper addressMapper;

    public AccountAddressResponse getAccountAddress() {
        return addressMapper.mapToDto(
                getAccountDetails()
                        .getAccount()
                        .getAddress()
        );
    }

    public AccountAddressResponse saveAccountAddress(AccountAddressRequest accountAddressRequest) {
        Account loggedAccount = getAccountDetails().getAccount();

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressRequest);

        accountAddress.setAccount(loggedAccount);
        addressRepository.save(accountAddress);
        return addressMapper.mapToDto(accountAddress);
    }

    public AccountAddressResponse updateAccountAddress(AccountAddressRequest accountAddressRequest) {
        AccountAddress loggedAccountAddress = getAccountDetails().getAccount().getAddress();

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressRequest);

        setupUpdateAddress(loggedAccountAddress, accountAddress);
        addressRepository.save(loggedAccountAddress);
        return addressMapper.mapToDto(loggedAccountAddress);
    }

    private AccountDetails getAccountDetails() {
        return (AccountDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void setupUpdateAddress(AccountAddress loggedAccountAddress, AccountAddress accountAddress) {
        loggedAccountAddress.setCity(accountAddress.getCity());
        loggedAccountAddress.setPostcode(accountAddress.getPostcode());
        loggedAccountAddress.setStreet(accountAddress.getStreet());

    }
}
