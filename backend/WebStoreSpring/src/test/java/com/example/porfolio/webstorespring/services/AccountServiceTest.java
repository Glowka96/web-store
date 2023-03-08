package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.porfolio.webstorespring.mappers.AccountMapper;
import com.example.porfolio.webstorespring.mappers.OrderMapper;
import com.example.porfolio.webstorespring.model.dto.accounts.AccountDto;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountRoles;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    private AccountDto accountDto;

    @BeforeEach()
    void initialization() {
        OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
        ReflectionTestUtils.setField(accountMapper, "orderMapper", orderMapper);

        AccountAddressMapper addressMapper = Mappers.getMapper(AccountAddressMapper.class);
        ReflectionTestUtils.setField(accountMapper, "accountAddressMapper", addressMapper);

        account = new Account();
        account.setId(1L);
        account.setFirstName("Test");
        account.setLastName("Dev");
        account.setPassword("Abcd123$");
        account.setAccountRoles(AccountRoles.USER);
        account.setEmail("test@test.pl");


        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setFirstName("Test");
        accountDto.setLastName("Dev");
        accountDto.setPassword("Abcd123$");

        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void shouldGetAccountById() {
        // given
        given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

        // when
        accountDto = underTest.getAccountById(1L);

        // then
        assertThat(accountDto).isNotNull();
        assertThat(accountDto.getId()).isEqualTo(1L);
        assertThat(accountDto.getFirstName()).isEqualTo("Test");
        assertThat(accountDto.getLastName()).isEqualTo("Dev");
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
        when(encoder.encode(accountDto.getPassword())).thenReturn("Abcd123$");
        accountDto = underTest.updateAccount(1L, accountDto);

        // then
        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        Account captureAccount = accountArgumentCaptor.getValue();
        AccountDto mappedAccount = accountMapper.mapToDto(captureAccount);

        assertThat(mappedAccount).isEqualTo(accountDto);
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