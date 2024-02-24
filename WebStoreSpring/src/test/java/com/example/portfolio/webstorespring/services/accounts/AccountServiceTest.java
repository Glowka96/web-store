package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.buildhelpers.accounts.RegistrationRequestBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.accounts.RoleBuilderHelper;
import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @Mock
    private RoleService roleService;
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
        AccountResponse result = underTest.getAccount();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Name");
        assertThat(result.getLastName()).isEqualTo("Lastname");
    }

    @Test
    void shouldSaveAccount() {
        // given
        RegistrationRequest registrationRequest = RegistrationRequestBuilderHelper.createRegistrationRequest();
        Role role = RoleBuilderHelper.createUserRole();

        given(roleService.findRoleByName(anyString())).willReturn(Set.of(role));

        // when
        Account result = underTest.saveAccount(registrationRequest);

        // then
        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        assertThat(result).isEqualTo(accountArgumentCaptor.getValue());
    }

    @Test
    void shouldSetEnableAccount() {
        // given
        Account account = createAccountWithRoleUserNoEnabled();

        // when
        underTest.setEnabledAccount(account);

        // then
        assertThat(account.getEnabled()).isTrue();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldSetNewAccountPassword() {
        // given
        Account account = createAccountWithRoleUser();
        String oldAccountPassword = account.getPassword();

        // when
        underTest.setNewAccountPassword(account, "new password");

        // then
        assertThat(account.getPassword()).isNotEqualTo(oldAccountPassword);
        verify(accountRepository, times(1)).save(any(Account.class));
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

    @Test
    void shouldFindAccountByEmail() {
        // given
        Account account = createAccountWithRoleUser();

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        // when
        Account result = underTest.findAccountByEmail("test");

        // then
        assertThat(result).isEqualTo(account);
    }

    @Test
    void willThrowWhenNotFindAccountByEmail () {
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.findAccountByEmail("test"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Account with email: test not found");
    }


    private void mockAuthentication(Account account) {
        AccountDetails accountDetails = new AccountDetails(account);
        when(authentication.getPrincipal()).thenReturn(accountDetails);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
}
