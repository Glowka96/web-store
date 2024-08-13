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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @Spy
    private AccountAddressMapper addressMapper = Mappers.getMapper(AccountAddressMapper.class);
    @InjectMocks
    private AccountAddressService underTest;

    @Test
    void shouldGetAccountAddress() {
        // given
        AccountDetails accountDetails = getAccountDetails();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));

        // when
        AccountAddressResponse foundAccountAddressResponse = underTest.getAccountAddress(accountDetails);

        // then
        assertThat(foundAccountAddressResponse).isNotNull();
        assertThat(foundAccountAddressResponse.getId()).isEqualTo(1);
        assertThat(foundAccountAddressResponse.getStreet()).isEqualTo(address.getStreet());
        assertThat(foundAccountAddressResponse.getCity()).isEqualTo(address.getCity());
        assertThat(foundAccountAddressResponse.getPostcode()).isEqualTo(address.getPostcode());
    }

    @Test
    void shouldSaveAccountAddress() {
        // given
        AccountDetails accountDetails = getAccountDetails();

        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();

        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));
        given(accountRepository.save(any(Account.class))).willReturn(accountDetails.getAccount());

        // when
        AccountAddressResponse savedAccountAddressResponse =
                underTest.saveAccountAddress(accountDetails, accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());
        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressResponse mappedAddress = addressMapper.mapToDto(captureAddress);

        assertThat(captureAddress.getId()).isEqualTo(accountDetails.getAccount().getId());
        assertThat(mappedAddress).isEqualTo(savedAccountAddressResponse);
    }

    @Test
    void shouldUpdateAccountAddress() {
        // given
        AccountDetails accountDetails = getAccountDetails();
        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));

        // when
        AccountAddressResponse updatedAccountAddressResponse =
                underTest.updateAccountAddress(accountDetails, accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressResponse mappedAccount = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAccount).isEqualTo(updatedAccountAddressResponse);
    }

    @Test
    void shouldDeleteAccountAddress() {
        AccountDetails accountDetails = getAccountDetails();
        AccountAddress address = createAccountAddress();

        given(addressRepository.findById(anyLong())).willReturn(Optional.of(address));

        underTest.deleteAccountAddress(accountDetails);

        verify(addressRepository).delete(address);
        verifyNoMoreInteractions(addressRepository);
    }

    @Test
    void shouldDeleteAccountAddress_whenDeleteAccount() {
        AccountDetails accountDetails = getAccountDetails();

        given(addressRepository.existsById(anyLong())).willReturn(true);

        underTest.deleteAccountAddressWhenDeleteAccount(accountDetails);

        verify(addressRepository, times(1)).deleteById(accountDetails.getAccount().getId());
        verifyNoMoreInteractions(addressRepository);
    }

    @Test
    void willThrowWhenAccountHasNoAddress_whenNotFoundAddressById() {
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
