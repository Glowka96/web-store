package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.AccountHasNoAddressException;
import com.example.portfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.portfolio.webstorespring.models.dtos.accounts.requests.AccountAddressRequest;
import com.example.portfolio.webstorespring.models.dtos.accounts.responses.AccountAddressResponse;
import com.example.portfolio.webstorespring.models.entities.accounts.Account;
import com.example.portfolio.webstorespring.models.entities.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentications.AccountDetails;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper.createAccountAddress;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper.createAccountAddressRequest;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountAddressServiceTest {

    @Mock
    private AccountAddressRepository addressRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountAddressService underTest;

    @Test
    void shouldGetAccountAddress() {
        AccountDetails accountDetails = getAccountDetails();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));

        AccountAddressResponse foundAccountAddressResponse = underTest.getByAccountDetails(accountDetails);

        assertNotNull(foundAccountAddressResponse);
        assertEquals(1L, foundAccountAddressResponse.id());
        assertEquals(address.getStreet(),foundAccountAddressResponse.street());
        assertEquals(address.getCity(), foundAccountAddressResponse.city());
        assertEquals(address.getPostcode(), foundAccountAddressResponse.postcode());
    }

    @Test
    void shouldSaveAccountAddress_whenUserAlreadyHaveAddress() {
        AccountDetails accountDetails = getAccountDetails();
        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));
        given(accountRepository.save(any(Account.class))).willReturn(accountDetails.getAccount());

        AccountAddressResponse savedAccountAddressResponse =
                underTest.save(accountDetails, accountAddressRequest);

        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());
        AccountAddressResponse mappedAddress = AccountAddressMapper.mapToResponse(accountAddressArgumentCaptor.getValue());

        assertEquals(accountDetails.getAccount().getId(), mappedAddress.id());
        assertEquals(savedAccountAddressResponse, mappedAddress);
    }

    @Test
    void shouldSaveAccountAddress_whenUserNotHaveSavedAddress() {
        AccountDetails accountDetails = getAccountDetails();
        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();

        given(addressRepository.findById(anyLong())).willReturn(Optional.empty());
        given(accountRepository.save(any(Account.class))).willReturn(accountDetails.getAccount());

        AccountAddressResponse savedAccountAddressResponse =
                underTest.save(accountDetails, accountAddressRequest);

        verify(addressRepository, times(1)).save(any(AccountAddress.class));
        assertEquals(accountAddressRequest.city(), savedAccountAddressResponse.city());
        assertEquals(accountAddressRequest.postcode(), savedAccountAddressResponse.postcode());
        assertEquals(accountAddressRequest.street(), savedAccountAddressResponse.street());
    }

    @Test
    void shouldUpdateAccountAddress() {
        AccountDetails accountDetails = getAccountDetails();
        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));

        AccountAddressResponse updatedAccountAddressResponse =
                underTest.update(accountDetails, accountAddressRequest);

        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddressResponse mappedAccount = AccountAddressMapper.mapToResponse(accountAddressArgumentCaptor.getValue());

        assertEquals(mappedAccount, updatedAccountAddressResponse);
    }

    @Test
    void shouldDeleteAccountAddress() {
        AccountDetails accountDetails = getAccountDetails();

        underTest.deleteByAccountDetails(accountDetails);

        verify(addressRepository).deleteById(accountDetails.getAccount().getId());
        verifyNoMoreInteractions(addressRepository);
    }

    @Test
    void willThrowWhenAccountHasNoAddressException_whenNotFoundAddressById() {
        AccountDetails accountDetails = getAccountDetails();
        given(addressRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getByAccountDetails(accountDetails))
                .isInstanceOf(AccountHasNoAddressException.class)
                .hasMessageContaining("Account: %s has no saved address".formatted(getAccountDetails().getAccount().getId()));
    }

    @NotNull
    private static AccountDetails getAccountDetails() {
        return new AccountDetails(make(a(BASIC_ACCOUNT)));
    }
}
