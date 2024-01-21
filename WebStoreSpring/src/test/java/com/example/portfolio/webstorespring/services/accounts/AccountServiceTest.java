package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import org.junit.jupiter.api.AfterEach;
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

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.createAccountRequest;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.createAccountWithRoleUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
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

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGetAccount() {
        // given
        Account account = createAccountWithRoleUser();
        mockAuthentication(account);

        // when
        AccountResponse actualAccount = underTest.getAccount();

        // then
        assertThat(actualAccount).isNotNull();
        assertThat(actualAccount.getId()).isEqualTo(1L);
        assertThat(actualAccount.getFirstName()).isEqualTo("Name");
        assertThat(actualAccount.getLastName()).isEqualTo("Lastname");
    }

    @Test
    void shouldUpdateAccount() {
        // given
        Account account = createAccountWithRoleUser();

        AccountRequest accountRequest = createAccountRequest();

        mockAuthentication(account);

        given(encoder.encode(anyString())).willReturn("hashedPassword");
        given(accountRepository.save(any(Account.class))).willReturn(account);

        // when
        AccountResponse updatedAccountResponse = underTest.updateAccount(accountRequest);

        // then
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        Account captureAccount = accountArgumentCaptor.getValue();
        AccountResponse mappedAccount = accountMapper.mapToDto(captureAccount);

        assertThat(mappedAccount).isEqualTo(updatedAccountResponse);
    }

    @Test
    void shouldDeleteAccount() {
        // given
        Account account = createAccountWithRoleUser();
        mockAuthentication(account);

        // when
        underTest.deleteAccount();

        // then
        verify(accountRepository, times(1)).delete(account);
    }

    private void mockAuthentication(Account account) {
        AccountDetails accountDetails = new AccountDetails(account);
        when(authentication.getPrincipal()).thenReturn(accountDetails);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
}
