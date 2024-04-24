package com.example.portfolio.webstorespring.services.accounts;

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

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountAddressBuilderHelper.createAccountAddressRequest;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.createAccountWithRoleUserAndAccountAddress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountAddressServiceTest {

    @Mock
    private AccountAddressRepository addressRepository;
    @Spy
    private AccountAddressMapper addressMapper = Mappers.getMapper(AccountAddressMapper.class);
    @InjectMocks
    private AccountAddressService underTest;

//    @AfterEach
//    void clearSecurityContext() {
//        SecurityContextHolder.clearContext();
//    }

    @Test
    void shouldGetAccountAddress() {
        // given
        Account account = createAccountWithRoleUserAndAccountAddress();
        AccountDetails accountDetails = new AccountDetails(account);

        // when
        AccountAddressResponse foundAccountAddressResponse = underTest.getAccountAddress(accountDetails);

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
        Account account = createAccountWithRoleUserAndAccountAddress();
        AccountDetails accountDetails = new AccountDetails(account);

        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();

        // when
        AccountAddressResponse savedAccountAddressResponse =
                underTest.saveAccountAddress(accountDetails, accountAddressRequest);

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
        Account account = createAccountWithRoleUserAndAccountAddress();
        AccountDetails accountDetails = new AccountDetails(account);

        AccountAddressRequest accountAddressRequest = createAccountAddressRequest();

        // when
        AccountAddressResponse updatedAccountAddressResponse =
                underTest.updateAccountAddress(accountDetails, accountAddressRequest);

        // then
        ArgumentCaptor<AccountAddress> accountAddressArgumentCaptor =
                ArgumentCaptor.forClass(AccountAddress.class);
        verify(addressRepository).save(accountAddressArgumentCaptor.capture());

        AccountAddress captureAddress = accountAddressArgumentCaptor.getValue();
        AccountAddressResponse mappedAccount = addressMapper.mapToDto(captureAddress);

        assertThat(mappedAccount).isEqualTo(updatedAccountAddressResponse);
    }
}
