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
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AccountAddressServiceTest {

    @Mock
    private AccountAddressRepository addressRepository;
    @Mock
    private AccountRepository accountRepository;
    @Spy
    private AccountAddressMapper addressMapper = Mappers.getMapper(AccountAddressMapper.class);
    @InjectMocks
    private AccountAddressService underTest;

    @Test
    void shouldGetAccountAddress() {
        AccountDetails accountDetails = getAccountDetails();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));

        AccountAddressResponse foundAccountAddressResponse = underTest.getAccountAddress(accountDetails);

        assertNotNull(foundAccountAddressResponse);
        assertEquals(1L, foundAccountAddressResponse.getId());
        assertEquals(address.getStreet(),foundAccountAddressResponse.getStreet());
        assertEquals(address.getCity(), foundAccountAddressResponse.getCity());
        assertEquals(address.getPostcode(), foundAccountAddressResponse.getPostcode());
    }

    @Test
    void shouldSaveAccountAddress() {
        AccountDetails accountDetails = getAccountDetails();
        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));
        given(accountRepository.save(any(Account.class))).willReturn(accountDetails.getAccount());

        AccountAddressResponse savedAccountAddressResponse =
                underTest.saveAccountAddress(accountDetails, accountAddressRequest);

        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());
        AccountAddressResponse mappedAddress = addressMapper.mapToDto(accountAddressArgumentCaptor.getValue());

        assertEquals(accountDetails.getAccount().getId(), mappedAddress.getId());
        assertEquals(savedAccountAddressResponse, mappedAddress);
    }

    @Test
    void shouldUpdateAccountAddress() {
        AccountDetails accountDetails = getAccountDetails();
        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));

        AccountAddressResponse updatedAccountAddressResponse =
                underTest.updateAccountAddress(accountDetails, accountAddressRequest);

        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddressResponse mappedAccount = addressMapper.mapToDto(accountAddressArgumentCaptor.getValue());

        assertEquals(mappedAccount, updatedAccountAddressResponse);
    }

    @Test
    void shouldDeleteAccountAddress() {
        AccountDetails accountDetails = getAccountDetails();

        underTest.deleteAccountAddress(accountDetails);

        verify(addressRepository).deleteById(accountDetails.getAccount().getId());
        verifyNoMoreInteractions(addressRepository);
    }

    @Test
    void willThrowWhenAccountHasNoAddressException_whenNotFoundAddressById() {
        AccountDetails accountDetails = getAccountDetails();
        given(addressRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getAccountAddress(accountDetails))
                .isInstanceOf(AccountHasNoAddressException.class)
                .hasMessageContaining("Account has no saved address");
    }

    @NotNull
    private static AccountDetails getAccountDetails() {
        return new AccountDetails(make(a(BASIC_ACCOUNT)));
    }
}
