package com.example.portfolio.webstorespring.security.auth;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
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
        account.setEnabled(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void shouldLoadUserByUsername() {
        when(accountRepository.findAccountByEmail(anyString())).thenReturn(Optional.of(account));

        UserDetails userDetails = underTest.loadUserByUsername("test@test.pl");

        assertThat(userDetails).isNotNull();
    }

    @Test
    void willThrowWhenAccountIsDisabled() {
        account.setEnabled(false);

        when(accountRepository.findAccountByEmail(anyString())).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> underTest.loadUserByUsername("test@test.pl"))
                .isInstanceOf(DisabledException.class)
                .hasMessageContaining("Your account is disabled");
    }
}
