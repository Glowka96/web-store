package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountAddressService {

    private final AccountAddressRepository addressRepository;
    private final AccountAddressMapper addressMapper;

    public AccountAddressResponse getAccountAddress(AccountDetails accountDetails) {
        return addressMapper.mapToDto(accountDetails.getAccount().getAddress());
    }

    @Transactional
    public AccountAddressResponse saveAccountAddress(AccountDetails accountDetails,
                                                     AccountAddressRequest accountAddressRequest) {
        Account loggedAccount = accountDetails.getAccount();

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressRequest);

        if (loggedAccount.getAddress() != null) {
            setupUpdateAddress(loggedAccount.getAddress(), accountAddress);
            addressRepository.save(loggedAccount.getAddress());
            return addressMapper.mapToDto(loggedAccount.getAddress());
        }

        accountAddress.setAccount(loggedAccount);
        addressRepository.save(accountAddress);
        return addressMapper.mapToDto(accountAddress);
    }

    @Transactional
    public AccountAddressResponse updateAccountAddress(AccountDetails accountDetails,
                                                       AccountAddressRequest accountAddressRequest) {
        AccountAddress loggedAccountAddress = accountDetails.getAccount().getAddress();

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressRequest);

        setupUpdateAddress(loggedAccountAddress, accountAddress);
        addressRepository.save(loggedAccountAddress);
        return addressMapper.mapToDto(loggedAccountAddress);
    }

    private void setupUpdateAddress(AccountAddress loggedAccountAddress, AccountAddress accountAddress) {
        loggedAccountAddress.setCity(accountAddress.getCity());
        loggedAccountAddress.setPostcode(accountAddress.getPostcode());
        loggedAccountAddress.setStreet(accountAddress.getStreet());

    }
}
