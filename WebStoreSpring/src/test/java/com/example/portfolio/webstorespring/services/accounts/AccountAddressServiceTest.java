package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.mappers.AccountAddressMapper;
import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountAddressRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountAddressResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.services.authentication.AccountDetails;
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

    private Account account;

    @BeforeEach
    void initialization() {
        AccountAddress accountAddress = AccountAddress.builder()
                .id(1L)
                .street("test 59/2")
                .postcode("99-999")
                .city("test")
                .build();

        account = Account.builder()
                .id(1L)
                .address(accountAddress)
                .build();

        mockAuthentication();
    }

    @Test
    void shouldGetAccountAddress() {
        // given
        // when
        AccountAddressResponse foundAccountAddressResponse = underTest.getAccountAddress();

        // then
        assertThat(foundAccountAddressResponse).isNotNull();
        assertThat(foundAccountAddressResponse.getId()).isEqualTo(1);
        assertThat(foundAccountAddressResponse.getStreet()).isEqualTo("test 59/2");
        assertThat(foundAccountAddressResponse.getCity()).isEqualTo("test");
        assertThat(foundAccountAddressResponse.getPostcode()).isEqualTo("99-999");
    }

    @Test
    void saveAccountAddress() {
        // given
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

    private void mockAuthentication() {
        AccountDetails accountDetails = new AccountDetails(account);
        when(authentication.getPrincipal()).thenReturn(accountDetails);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    private AccountAddressRequest createAccountAddressRequest() {
        return AccountAddressRequest.builder()
                .street("test 59/2")
                .city("test")
                .postcode("99-999")
                .build();
    }
}
