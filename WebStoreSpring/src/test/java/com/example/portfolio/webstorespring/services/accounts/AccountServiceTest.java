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

import java.util.Optional;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        // given
        AccountDetails accountDetails = getAccountDetails();

        // when
        AccountResponse result = underTest.getAccount(accountDetails);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo(accountDetails.getAccount().getFirstName());
        assertThat(result.getLastName()).isEqualTo(accountDetails.getAccount().getLastName());
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
        Account account = make(a(BASIC_ACCOUNT).but(with(ENABLED, Boolean.FALSE)));

        // when
        underTest.setEnabledAccount(account);

        // then
        assertThat(account.getEnabled()).isTrue();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldSetNewAccountPassword() {
        // given
        Account account = make(a(BASIC_ACCOUNT));
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
        Account account = make(a(BASIC_ACCOUNT));

        AccountRequest accountRequest = createAccountRequest();
        AccountDetails accountDetails = new AccountDetails(account);

        given(encoder.encode(anyString())).willReturn("hashedPassword");
        given(accountRepository.save(any(Account.class))).willReturn(account);

        // when
        AccountResponse updatedAccountResponse = underTest.updateAccount(accountDetails, accountRequest);

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
        AccountDetails accountDetails = getAccountDetails();

        // when
        underTest.deleteAccount(accountDetails);

        // then
        verify(accountRepository, times(1)).delete(accountDetails.getAccount());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldFindAccountByEmail() {
        // given
        Account account = make(a(BASIC_ACCOUNT));

        given(accountRepository.findByEmail(anyString())).willReturn(Optional.of(account));

        // when
        Account result = underTest.findAccountByEmail("test");

        // then
        assertThat(result).isEqualTo(account);
    }

    @Test
    void willThrowUsernameNotFound_whenNotFindAccountByEmail() {
        given(accountRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.findAccountByEmail("test"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Account with email: test not found");
    }

    @NotNull
    private static AccountDetails getAccountDetails() {
        return new AccountDetails(make(a(BASIC_ACCOUNT)));
    }
}
