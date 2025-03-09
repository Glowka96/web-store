package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.AccountHasNoAddressException;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.AccountAddressRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AccountAddressResponse;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.portfolio.webstorespring.mappers.AccountAddressMapper.mapToDto;
import static com.example.portfolio.webstorespring.mappers.AccountAddressMapper.mapToEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountAddressService {

    private final AccountAddressRepository addressRepository;
    private final AccountRepository accountRepository;

    public AccountAddressResponse getByAccountDetails(AccountDetails accountDetails) {
        log.info("Fetching address for account ID: {}", accountDetails.getAccount().getId());
        return mapToDto(findByAccountDetails(accountDetails));
    }

    @Transactional
    public AccountAddressResponse save(AccountDetails accountDetails,
                                       AccountAddressRequest accountAddressRequest) {
        log.info("Saving address for account ID: {}", accountDetails.getAccount().getId());
        Account loggedAccount = accountDetails.getAccount();
        loggedAccount = accountRepository.save(loggedAccount);

        log.debug("Mapping request: {}", accountAddressRequest);
        AccountAddress accountAddress = mapToEntity(accountAddressRequest);

        var finalLoggedAccount = loggedAccount;
        return addressRepository.findById(accountDetails.getAccount().getId()).map(
                existAddress -> {
                    log.info("Address exists for account ID: {}, updating it", accountDetails.getAccount().getId());
                    setupUpdatedAddress(existAddress, accountAddress);
                    addressRepository.save(existAddress);
                    log.debug("Updated address");
                    return mapToDto(existAddress);
                }
        ).orElseGet(() -> {
            log.info("No address exists for account ID: {}, saving it", accountDetails.getAccount().getId());
            accountAddress.setAccount(finalLoggedAccount);
            addressRepository.save(accountAddress);
            log.debug("Created new address");
            return mapToDto(accountAddress);
        });
    }

    @Transactional
    public AccountAddressResponse update(AccountDetails accountDetails,
                                         AccountAddressRequest accountAddressRequest) {
        log.info("Updating address for account ID: {}", accountDetails.getAccount().getId());
        AccountAddress loggedAccountAddress = findByAccountDetails(accountDetails);

        log.debug("Mapped request: {}", accountAddressRequest);
        AccountAddress accountAddress = mapToEntity(accountAddressRequest);

        setupUpdatedAddress(loggedAccountAddress, accountAddress);
        addressRepository.save(loggedAccountAddress);
        log.info("Updated address");
        return mapToDto(loggedAccountAddress);
    }

    public void deleteByAccountDetails(AccountDetails accountDetails) {
        log.info("Deleting address for account ID: {}", accountDetails.getAccount().getId());
        addressRepository.deleteById(accountDetails.getAccount().getId());
    }

    private AccountAddress findByAccountDetails(AccountDetails accountDetails) {
        log.debug("Finding address for account ID: {}", accountDetails.getAccount().getId());
        return addressRepository.findById(accountDetails.getAccount().getId())
                .orElseThrow(() -> new AccountHasNoAddressException(accountDetails.getAccount().getId()));
    }

    private void setupUpdatedAddress(AccountAddress loggedAccountAddress, AccountAddress accountAddress) {
        loggedAccountAddress.setCity(accountAddress.getCity());
        loggedAccountAddress.setPostcode(accountAddress.getPostcode());
        loggedAccountAddress.setStreet(accountAddress.getStreet());
    }
}
