package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
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

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper.createAccountAddressRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountAddressServiceTest {

    @Mock
    private AccountAddressRepository addressRepository;
    @Spy
    private AccountAddressMapper addressMapper = Mappers.getMapper(AccountAddressMapper.class);
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private AccountAddressService underTest;

    @Test
    void shouldGetAccountAddress() {
        // given
        Account account = AccountBuilderHelper.createAccountWithRoleUserAndAccountAddress();
        mockAuthentication(account);

        // when
        AccountAddressResponse foundAccountAddressResponse = underTest.getAccountAddress();

        // then
        assertThat(foundAccountAddressResponse).isNotNull();
        assertThat(foundAccountAddressResponse.getId()).isEqualTo(1);
        assertThat(foundAccountAddressResponse.getStreet()).isEqualTo(account.getAddress().getStreet());
        assertThat(foundAccountAddressResponse.getCity()).isEqualTo(account.getAddress().getCity());
        assertThat(foundAccountAddressResponse.getPostcode()).isEqualTo(account.getAddress().getPostcode());
    }

    @Test
    void saveAccountAddress() {
        // given
        Account account = AccountBuilderHelper.createAccountWithRoleUserAndAccountAddress();
        mockAuthentication(account);

        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();

        // when
        AccountAddressResponse savedAccountAddressResponse = underTest.saveAccountAddress(accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressResponse mappedAddress = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAddress).isEqualTo(savedAccountAddressResponse);
    }

    @Test
    void updateAccountAddress() {
        // given
        Account account = AccountBuilderHelper.createAccountWithRoleUserAndAccountAddress();
        mockAuthentication(account);

        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();

        // when
        AccountAddressResponse updatedAccountAddressResponse = underTest.updateAccountAddress(accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressResponse mappedAccount = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAccount).isEqualTo(updatedAccountAddressResponse);
    }

    private void mockAuthentication(Account account) {
        AccountDetails accountDetails = new AccountDetails(account);
        when(authentication.getPrincipal()).thenReturn(accountDetails);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
}
