package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.porfolio.webstorespring.mappers.AccountMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountRequest;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountResponse;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.porfolio.webstorespring.services.accounts.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @InjectMocks
    private AccountService underTest;
    private Account account;
    private AccountResponse accountResponse;
    private AccountRequest accountRequest;

    @BeforeEach()
    void initialization() {
        AccountAddressMapper addressMapper = Mappers.getMapper(AccountAddressMapper.class);
        ReflectionTestUtils.setField(accountMapper, "accountAddressMapper", addressMapper);

        account = new Account();
        account.setId(1L);
        account.setFirstName("Test");
        account.setLastName("Dev");

        accountRequest = new AccountRequest();
        accountRequest.setFirstName("Test");
        accountRequest.setLastName("Dev");
        accountRequest.setPassword("Abcd123$");
        accountRequest.setEmail("test@test.pl");


        accountResponse = new AccountResponse();
        accountResponse.setId(1L);
        accountResponse.setFirstName("Test");
        accountResponse.setLastName("Dev");
    }

    @Test
    void shouldGetAccountById() {
        // given
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        // when
        accountResponse = underTest.getAccountById(1L);

        // then
        assertThat(accountResponse).isNotNull();
        assertThat(accountResponse.getId()).isEqualTo(1L);
        assertThat(accountResponse.getFirstName()).isEqualTo("Test");
        assertThat(accountResponse.getLastName()).isEqualTo("Dev");
    }

    @Test
    void willThrowWhenAccountIdNotFound() {
        // given
        given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getAccountById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Account with id 1 not found");
    }

    @Test
    void shouldUpdateAccount() {
        // given
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        // when
        accountResponse = underTest.updateAccount(1L, accountRequest);

        // then
        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        Account captureAccount = accountArgumentCaptor.getValue();
        AccountResponse mappedAccount = accountMapper.mapToDto(captureAccount);

        assertThat(mappedAccount).isEqualTo(accountResponse);
    }

    @Test
    void shouldDeleteAccountById() {
        // given
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        // when
        underTest.deleteAccountById(1L);

        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).delete(account);
    }
}