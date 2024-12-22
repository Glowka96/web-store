package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.buildhelpers.accounts.RegistrationRequestBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.accounts.RoleBuilderHelper;
import com.example.portfolio.webstorespring.config.providers.AccountImageUrlProvider;
import com.example.portfolio.webstorespring.config.providers.AdminCredentialsProvider;
import com.example.portfolio.webstorespring.mappers.AccountMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.RegistrationRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.request.UpdatePasswordRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
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
    private AdminCredentialsProvider adminCredentialsProvider;
    @Mock
    private AccountImageUrlProvider accountImageUrlProvider;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private AccountService underTest;

    private static final String ADMIN_EMAIL = "mockadmin@example.com";
    private static final String HASHED_PASSWORD = "hashedPassword";
    @Test
    void shouldGetAccount() {
        AccountDetails accountDetails = getAccountDetails();

        AccountResponse result = underTest.getByAccountDetails(accountDetails);

        assertNotNull(result);
        assertEquals(accountDetails.getAccount().getId(), result.id());
        assertEquals(accountDetails.getAccount().getFirstName(), result.firstName());
        assertEquals(accountDetails.getAccount().getLastName(), result.lastName());
        assertEquals(accountDetails.getAccount().getEmail(), result.email());
        assertEquals(accountDetails.getAccount().getImageUrl(), result.imageUrl());
    }

    @Test
    void shouldSaveAccount() {
        RegistrationRequest registrationRequest = RegistrationRequestBuilderHelper.createRegistrationRequest();
        Role role = RoleBuilderHelper.createUserRole();

        given(roleService.findByName(anyString())).willReturn(Set.of(role));
        given(encoder.encode(anyString())).willReturn(HASHED_PASSWORD);
        given(accountRepository.save(any(Account.class))).willAnswer(invocation -> invocation.getArgument(0));

        Account result = underTest.save(registrationRequest);

        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        assertEquals(accountArgumentCaptor.getValue(), result);
    }

    @Test
    void shouldInitializeAdminAccount_whenAdminEmailNotExist() {
        given(adminCredentialsProvider.getEmail()).willReturn(ADMIN_EMAIL);
        given(accountImageUrlProvider.getUrl()).willReturn(getACCOUNT_IMAGE_URL());
        given(accountRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);

        underTest.initializeAdminAccount();

        verify(accountRepository, times(1)).existsByEmail(anyString());
        verify(accountRepository, times(1)).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldNotInitializeAdminAccount_whenAdminEmailIsExist() {
        given(adminCredentialsProvider.getEmail()).willReturn(ADMIN_EMAIL);
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
    }

    @Test
    void shouldSetNewAccountPassword() {
        Account account = make(a(BASIC_ACCOUNT));
        String oldAccountPassword = account.getPassword();
        given(encoder.encode(anyString())).willReturn(HASHED_PASSWORD);

        underTest.setNewAccountPassword(account, "new password");
        assertNotEquals(oldAccountPassword, account.getPassword());
    }

    @Test
    void shouldUpdateAccount() {
        Account account = make(a(BASIC_ACCOUNT));

        AccountRequest accountRequest = createAccountRequest();

        given(accountRepository.save(any(Account.class))).willReturn(account);

        AccountResponse updatedAccountResponse = underTest.update(new AccountDetails(account), accountRequest);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        AccountResponse mappedAccount = AccountMapper.mapToDto(accountArgumentCaptor.getValue());

        assertEquals(mappedAccount, updatedAccountResponse);
    }

    @Test
    void shouldUpdatePassword() {
        Account account = make(a(BASIC_ACCOUNT));
        String newPassword = "new password";
        Map<String, Object> excepted = new HashMap<>();
        excepted.put("message", "Password updated successfully.");

        given(encoder.encode(anyString())).willReturn(HASHED_PASSWORD);

        Map<String, Object> result = underTest.updatePassword(new AccountDetails(account), new UpdatePasswordRequest(account.getPassword(), newPassword));

        assertEquals(excepted, result);
        assertEquals(HASHED_PASSWORD, account.getPassword());
        assertNotEquals(newPassword, account.getPassword());
    }

    @Test
    void shouldDeleteAccount() {
        AccountDetails accountDetails = getAccountDetails();

        underTest.delete(accountDetails);

        verify(accountRepository, times(1)).delete(accountDetails.getAccount());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldFindAccountByEmail() {
        Account account = make(a(BASIC_ACCOUNT));

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        Account result = underTest.findByEmail("test");

        assertEquals(account, result);
    }

    @Test
    void willThrowUsernameNotFoundException_whenNotFindAccountByEmail() {
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findByEmail("test"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Account with email: test not found");
    }

    @NotNull
    private static AccountDetails getAccountDetails() {
        return new AccountDetails(make(a(BASIC_ACCOUNT)));
    }
}
