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
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountAddressService addressService;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private RoleService roleService;
    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @InjectMocks
    private AccountService underTest;

    @Test
    void shouldGetAccount() {
        AccountDetails accountDetails = getAccountDetails();

        AccountResponse result = underTest.getAccount(accountDetails);

        assertNotNull(result);
        assertEquals(accountDetails.getAccount().getId(), result.getId());
        assertEquals(accountDetails.getAccount().getFirstName(), result.getFirstName());
        assertEquals(accountDetails.getAccount().getLastName(), result.getLastName());
        assertEquals(accountDetails.getAccount().getEmail(), result.getEmail());
        assertEquals(accountDetails.getAccount().getImageUrl(), result.getImageUrl());
    }

    @Test
    void shouldSaveAccount() {
        RegistrationRequest registrationRequest = RegistrationRequestBuilderHelper.createRegistrationRequest();
        Role role = RoleBuilderHelper.createUserRole();

        given(roleService.findRoleByName(anyString())).willReturn(Set.of(role));

        Account result = underTest.saveAccount(registrationRequest);

        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        assertEquals(accountArgumentCaptor.getValue(), result);
    }

    @Test
    void shouldInitializeAdminAccount_whenAdminEmailNotExist() {
        ReflectionTestUtils.setField(underTest, "adminEmail", "mockadmin@example.com");

        given(accountRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);

        underTest.initializeAdminAccount();

        verify(accountRepository, times(1)).existsByEmail(anyString());
        verify(accountRepository, times(1)).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldNotInitializeAdminAccount_whenAdminEmailIsExist() {
        ReflectionTestUtils.setField(underTest, "adminEmail", "mockadmin@example.com");

        given(accountRepository.existsByEmail(anyString())).willReturn(Boolean.TRUE);

        underTest.initializeAdminAccount();

        verify(accountRepository, times(1)).existsByEmail(anyString());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldSetEnableAccount() {
        Account account = make(a(BASIC_ACCOUNT).but(with(ENABLED, Boolean.FALSE)));

        underTest.setEnabledAccount(account);

        assertTrue(account.getEnabled());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldSetNewAccountPassword() {
        Account account = make(a(BASIC_ACCOUNT));
        String oldAccountPassword = account.getPassword();

        underTest.setNewAccountPassword(account, "new password");

        assertNotEquals(oldAccountPassword, account.getPassword());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldUpdateAccount() {
        Account account = make(a(BASIC_ACCOUNT));

        AccountRequest accountRequest = createAccountRequest();
        AccountDetails accountDetails = new AccountDetails(account);

        given(encoder.encode(anyString())).willReturn("hashedPassword");
        given(accountRepository.save(any(Account.class))).willReturn(account);

        AccountResponse updatedAccountResponse = underTest.updateAccount(accountDetails, accountRequest);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        AccountResponse mappedAccount = accountMapper.mapToDto(accountArgumentCaptor.getValue());

        assertEquals(mappedAccount, updatedAccountResponse);
    }

    @Test
    void shouldDeleteAccount() {
        AccountDetails accountDetails = getAccountDetails();

        underTest.deleteAccount(accountDetails);

        verify(accountRepository, times(1)).delete(accountDetails.getAccount());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldFindAccountByEmail() {
        Account account = make(a(BASIC_ACCOUNT));

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        Account result = underTest.findAccountByEmail("test");

        assertEquals(account, result);
    }

    @Test
    void willThrowUsernameNotFoundException_whenNotFindAccountByEmail() {
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findAccountByEmail("test"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Account with email: test not found");
    }

    @NotNull
    private static AccountDetails getAccountDetails() {
        return new AccountDetails(make(a(BASIC_ACCOUNT)));
    }
}
