package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.AccountHasNoAddressException;
import com.example.portfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountAddressService {

    private final AccountAddressRepository addressRepository;
    private final AccountAddressMapper addressMapper;
    private final AccountRepository accountRepository;

    public AccountAddressResponse getAccountAddress(AccountDetails accountDetails) {
        AccountAddress accountAddress = findAddressByAccountId(accountDetails);
        return addressMapper.mapToDto(accountAddress);
    }

    @Transactional
    public AccountAddressResponse saveAccountAddress(AccountDetails accountDetails,
                                                     AccountAddressRequest accountAddressRequest) {
        Account loggedAccount = accountDetails.getAccount();
        loggedAccount = accountRepository.save(loggedAccount);

        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressRequest);

        Optional<AccountAddress> addressOptional = addressRepository.findById(accountDetails.getAccount().getId());
        if (addressOptional.isPresent()) {
            AccountAddress presentAddress = addressOptional.get();
            setupUpdateAddress(presentAddress, accountAddress);
            addressRepository.save(presentAddress);
            return addressMapper.mapToDto(presentAddress);
        }

        accountAddress.setAccount(loggedAccount);
        addressRepository.save(accountAddress);
        return addressMapper.mapToDto(accountAddress);
    }

    @Transactional
    public AccountAddressResponse updateAccountAddress(AccountDetails accountDetails,
                                                       AccountAddressRequest accountAddressRequest) {
        AccountAddress loggedAccountAddress = findAddressByAccountId(accountDetails);
        AccountAddress accountAddress = addressMapper.mapToEntity(accountAddressRequest);
        setupUpdateAddress(loggedAccountAddress, accountAddress);
        addressRepository.save(loggedAccountAddress);

        return addressMapper.mapToDto(loggedAccountAddress);
    }

    @Transactional
    public void deleteAccountAddress(AccountDetails accountDetails) {
        AccountAddress foundAddress = findAddressByAccountId(accountDetails);
        addressRepository.delete(foundAddress);
    }

    public void deleteAccountAddressWhenDeleteAccount(AccountDetails accountDetails) {
        if(addressRepository.existsById(accountDetails.getAccount().getId())){
            addressRepository.deleteById(accountDetails.getAccount().getId());
        }
    }

    private AccountAddress findAddressByAccountId(AccountDetails accountDetails) {
        return addressRepository.findById(accountDetails.getAccount().getId())
                .orElseThrow(AccountHasNoAddressException::new);
    }

    private void setupUpdateAddress(AccountAddress loggedAccountAddress, AccountAddress accountAddress) {
        loggedAccountAddress.setCity(accountAddress.getCity());
        loggedAccountAddress.setPostcode(accountAddress.getPostcode());
        loggedAccountAddress.setStreet(accountAddress.getStreet());

    }
}
