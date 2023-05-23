package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressRequest;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.porfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.porfolio.webstorespring.services.accounts.AccountAddressService;
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
    }

    @Test
    void shouldGetAccountAddressByAccountId() {
        // given
        given(addressRepository.findAccountAddressByAccount_Id(anyLong())).willReturn(Optional.of(accountAddress));

        // when
        accountAddressRequest = underTest.getAccountAddressByAccountId(1L);

        // then
        assertThat(accountAddressRequest).isNotNull();
        assertThat(accountAddressRequest.getId()).isEqualTo(1);
        assertThat(accountAddressRequest.getStreet()).isEqualTo("test 59/2");
        assertThat(accountAddressRequest.getCity()).isEqualTo("test");
        assertThat(accountAddressRequest.getPostcode()).isEqualTo("99-999");
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

        accountAddressRequest = underTest.saveAccountAddress(1L, accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressRequest mappedAddress = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAddress).isEqualTo(accountAddressRequest);
    }

    @Test
    void updateAccountAddress() {
        // given
        given(addressRepository.findAccountAddressByAccount_Id(anyLong())).willReturn(Optional.of(accountAddress));

        // when
        accountAddressRequest = underTest.updateAccountAddress(1L, accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressRequest mappedAccount = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAccount).isEqualTo(accountAddressRequest);
    }
}