package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountAddressServiceTest {

    @Mock
    private AccountAddressRepository addressRepository;
    @Spy
    private AccountAddressMapper addressMapper = Mappers.getMapper(AccountAddressMapper.class);
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountAddressService underTest;

    private AccountAddress accountAddress;
    private AccountAddressRequest accountAddressRequest;
    private Account account;

    @BeforeEach
    void initialization() {
        account = new Account();
        account.setId(1L);

        accountAddress = new AccountAddress();
        accountAddress.setId(1L);
        accountAddress.setStreet("test 59/2");
        accountAddress.setCity("test");
        accountAddress.setPostcode("99-999");
        accountAddress.setAccount(account);

        accountAddressRequest = new AccountAddressRequest();
        accountAddressRequest.setStreet("test 59/2");
        accountAddressRequest.setCity("test");
        accountAddressRequest.setPostcode("99-999");
    }

    @Test
    void shouldGetAccountAddressByAccountId() {
        // given
        given(addressRepository.findAccountAddressByAccount_Id(anyLong())).willReturn(Optional.of(accountAddress));

        // when
        AccountAddressResponse foundAccountAddressResponse = underTest.getAccountAddressByAccountId(1L);

        // then
        assertThat(foundAccountAddressResponse).isNotNull();
        assertThat(foundAccountAddressResponse.getId()).isEqualTo(1);
        assertThat(foundAccountAddressResponse.getStreet()).isEqualTo("test 59/2");
        assertThat(foundAccountAddressResponse.getCity()).isEqualTo("test");
        assertThat(foundAccountAddressResponse.getPostcode()).isEqualTo("99-999");
    }

    @Test
    void willThrowWhenAccountAddressIdNotFound() {
        // given
        given(addressRepository.findAccountAddressByAccount_Id(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getAccountAddressByAccountId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Address with account id 1 not found");
    }

    @Test
    void saveAccountAddress() {
        // given
        // when
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(addressRepository.save(any(AccountAddress.class))).thenReturn(accountAddress);

        AccountAddressResponse savedAccountAddressResponse = underTest.saveAccountAddress(1L, accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressResponse mappedAddress = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAddress).isEqualTo(savedAccountAddressResponse);
    }

    @Test
    void updateAccountAddress() {
        // given
        given(addressRepository.findAccountAddressByAccount_Id(anyLong())).willReturn(Optional.of(accountAddress));

        // when
        AccountAddressResponse updatedAccountAddressResponse = underTest.updateAccountAddress(1L, accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressResponse mappedAccount = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAccount).isEqualTo(updatedAccountAddressResponse);
    }
}
