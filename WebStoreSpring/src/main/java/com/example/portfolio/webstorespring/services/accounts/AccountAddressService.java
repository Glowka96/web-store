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

@Service
@RequiredArgsConstructor
public class AccountAddressService {

    private final AccountAddressRepository addressRepository;
    private final AccountRepository accountRepository;

    public AccountAddressResponse getByAccountDetails(AccountDetails accountDetails) {
        AccountAddress accountAddress = findByAccountDetails(accountDetails);
        return AccountAddressMapper.mapToDto(accountAddress);
    }

    @Transactional
    public AccountAddressResponse save(AccountDetails accountDetails,
                                       AccountAddressRequest accountAddressRequest) {
        Account loggedAccount = accountDetails.getAccount();
        loggedAccount = accountRepository.save(loggedAccount);

        AccountAddress accountAddress = AccountAddressMapper.mapToEntity(accountAddressRequest);

        var finalLoggedAccount = loggedAccount;
        return addressRepository.findById(accountDetails.getAccount().getId()).map(
                accountAddress1 -> {
                    setupUpdatedAddress(accountAddress1, accountAddress);
                    addressRepository.save(accountAddress1);
                    return AccountAddressMapper.mapToDto(accountAddress1);
                }
        ).orElseGet(() -> {
            accountAddress.setAccount(finalLoggedAccount);
            addressRepository.save(accountAddress);
            return AccountAddressMapper.mapToDto(accountAddress);
        });
    }

    @Transactional
    public AccountAddressResponse update(AccountDetails accountDetails,
                                         AccountAddressRequest accountAddressRequest) {
        AccountAddress loggedAccountAddress = findByAccountDetails(accountDetails);
        AccountAddress accountAddress = AccountAddressMapper.mapToEntity(accountAddressRequest);
        setupUpdatedAddress(loggedAccountAddress, accountAddress);
        addressRepository.save(loggedAccountAddress);

        return AccountAddressMapper.mapToDto(loggedAccountAddress);
    }

    public void deleteByAccountDetails(AccountDetails accountDetails) {
        addressRepository.deleteById(accountDetails.getAccount().getId());
    }

    private AccountAddress findByAccountDetails(AccountDetails accountDetails) {
        return addressRepository.findById(accountDetails.getAccount().getId())
                .orElseThrow(() -> new AccountHasNoAddressException(accountDetails.getAccount().getId()));
    }

    private void setupUpdatedAddress(AccountAddress loggedAccountAddress, AccountAddress accountAddress) {
        loggedAccountAddress.setCity(accountAddress.getCity());
        loggedAccountAddress.setPostcode(accountAddress.getPostcode());
        loggedAccountAddress.setStreet(accountAddress.getStreet());

    }
}
