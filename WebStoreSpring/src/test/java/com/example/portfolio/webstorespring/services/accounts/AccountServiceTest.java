package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @InjectMocks
    private AccountService underTest;
    private Account account;

    @BeforeEach()
    void initialization() {
        account = Account.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Dev")
                .build();

        mockAuthentication();
    }

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetAccount() {
        // given
        // when
        AccountResponse actualAccount = underTest.getAccount();

        // then
        assertThat(actualAccount).isNotNull();
        assertThat(actualAccount.getId()).isEqualTo(1L);
        assertThat(actualAccount.getFirstName()).isEqualTo("Test");
        assertThat(actualAccount.getLastName()).isEqualTo("Dev");
    }

    @Test
    void shouldUpdateAccount() {
        // given
        AccountRequest accountRequest = AccountRequest.builder()
                .firstName("Test")
                .lastName("Dev")
                .password("Abcd123$")
                .build();

        when(encoder.encode(anyString())).thenReturn("hashedPassword");
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // when
        AccountResponse updatedAccountResponse = underTest.updateAccount(accountRequest);

        // then
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        Account captureAccount = accountArgumentCaptor.getValue();
        AccountResponse mappedAccount = accountMapper.mapToDto(captureAccount);

        assertThat(mappedAccount.getFirstName()).isEqualTo(updatedAccountResponse.getFirstName());
        assertThat(mappedAccount.getLastName()).isEqualTo(updatedAccountResponse.getLastName());
        assertThat(captureAccount.getPassword()).isEqualTo("hashedPassword");
    }

    @Test
    void shouldDeleteAccount() {
        // given
        // when
        underTest.deleteAccount();

        // then
        verify(accountRepository, times(1)).delete(account);
    }

    private void mockAuthentication() {
        AccountDetails accountDetails = new AccountDetails(account);
        when(authentication.getPrincipal()).thenReturn(accountDetails);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
}
