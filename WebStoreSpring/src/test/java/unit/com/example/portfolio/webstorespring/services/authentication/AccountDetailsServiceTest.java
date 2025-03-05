package com.example.portfolio.webstorespring.services.authentication;

import com.example.portfolio.webstorespring.models.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.ENABLED;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDetailsServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountDetailsService underTest;

    private static final String EMAIL = "test@test.pl";

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldLoadUserByUsername() {
        Account account = make(a(BASIC_ACCOUNT));
        setupSecutiryContext(account);

        given(accountRepository.findWithRolesByEmail(anyString())).willReturn(Optional.of(account));

        UserDetails userDetails = underTest.loadUserByUsername(EMAIL);

        assertNotNull(userDetails);
    }

    @Test
    void willThrowDisableException_whenAccountIsDisabled() {
        Account account = make(a(BASIC_ACCOUNT).but(with(ENABLED, Boolean.FALSE)));
        setupSecutiryContext(account);

        when(accountRepository.findWithRolesByEmail(anyString())).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> underTest.loadUserByUsername(EMAIL))
                .isInstanceOf(DisabledException.class)
                .hasMessageContaining("Your account is disabled");
    }

    @Test
    void willThrowUsernameNotFoundException_whenAccountEmailNotFound() {
        assertThatThrownBy(() -> underTest.loadUserByUsername(EMAIL))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Account with email: " + EMAIL + " not found");
    }

    private void setupSecutiryContext(Account account) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

}
