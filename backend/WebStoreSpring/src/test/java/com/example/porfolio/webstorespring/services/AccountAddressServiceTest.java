package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountAddressDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.porfolio.webstorespring.repositories.AccountAddressRepository;
import com.example.porfolio.webstorespring.repositories.AccountRepository;
import com.example.porfolio.webstorespring.services.auth.AccountDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
    private AccountAddressDto accountAddressDto;
    private Account account;

    @BeforeEach
    void initialization() {
        account = new Account();
        account.setId(1L);

        accountAddress = new AccountAddress();
        accountAddress.setId(1L);
        accountAddress.setAddress("test 59");
        accountAddress.setCity("test");
        accountAddress.setPostcode("99-999");
        accountAddress.setAccount(account);

        accountAddressDto = new AccountAddressDto();

        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void shouldGetAccountAddressByAccountId() {
        // given
        given(addressRepository.findAccountAddressByAccount_Id(anyLong())).willReturn(Optional.of(accountAddress));

        // when
        accountAddressDto = underTest.getAccountAddressByAccountId(1L);

        // then
        assertThat(accountAddressDto).isNotNull();
        assertThat(accountAddressDto.getId()).isEqualTo(1);
        assertThat(accountAddressDto.getAddress()).isEqualTo("test 59");
        assertThat(accountAddressDto.getCity()).isEqualTo("test");
        assertThat(accountAddressDto.getPostcode()).isEqualTo("99-999");
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

        accountAddressDto = underTest.saveAccountAddress(1L, accountAddressDto);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressDto mappedAddress = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAddress).isEqualTo(accountAddressDto);
    }

    @Test
    void updateAccountAddress() {
        // given
        given(addressRepository.findAccountAddressByAccount_Id(anyLong())).willReturn(Optional.of(accountAddress));

        // when
        accountAddressDto = underTest.updateAccountAddress(1L, accountAddressDto);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressDto mappedAccount = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAccount).isEqualTo(accountAddressDto);
    }
}