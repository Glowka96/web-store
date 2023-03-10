package com.example.porfolio.webstorespring.services.auth;

import com.example.porfolio.webstorespring.exceptions.AccountCanNotModifiedException;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDetailsServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountDetailsService underTest;

    private Account account;

    @BeforeEach
    void initialization() {
        account = new Account();
        account.setId(1L);
        account.setEmail("test@test.pl");

        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void shouldLoadUserByUsername() {
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(account));

        UserDetails userDetails = underTest.loadUserByUsername(account.getEmail());

        assertThat(userDetails).isNotNull();
    }

    @Test
    void shouldSuccessIsValidAuthLoggedUser() {
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(account));

        boolean isValidAuth = underTest.isValidAuthLoggedUser(account.getId());

        assertThat(isValidAuth).isTrue();
    }

    @Test
    void willThrowWhenIsNotValidAuthLoggedUser() {
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> underTest.isValidAuthLoggedUser(2L))
                .isInstanceOf(AccountCanNotModifiedException.class)
                .hasMessageContaining("You can only used or modified your own account!");
    }
}