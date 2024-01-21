package com.example.portfolio.webstorespring.services.authentication;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
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

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.AccountBuilderHelper.createAccountWithRoleUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDetailsServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountDetailsService underTest;

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldLoadUserByUsername() {
        // given
        Account account = createAccountWithRoleUser();
        setupSecutiryContext(account);

        given(accountRepository.findAccountByEmail(anyString())).willReturn(Optional.of(account));

        // when
        UserDetails userDetails = underTest.loadUserByUsername("test@test.pl");

        // then
        assertThat(userDetails).isNotNull();
    }

    @Test
    void willThrowWhenAccountIsDisabled() {
        // given
        Account account = createAccountWithRoleUser();
        account.setEnabled(false);
        setupSecutiryContext(account);

        // when
        when(accountRepository.findAccountByEmail(anyString())).thenReturn(Optional.of(account));

        // then
        assertThatThrownBy(() -> underTest.loadUserByUsername("test@test.pl"))
                .isInstanceOf(DisabledException.class)
                .hasMessageContaining("Your account is disabled");
    }

    private void setupSecutiryContext(Account account) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(new AccountDetails(account), null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

}
