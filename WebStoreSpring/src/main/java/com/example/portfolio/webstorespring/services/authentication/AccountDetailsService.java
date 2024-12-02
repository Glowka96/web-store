package com.example.portfolio.webstorespring.services.authentication;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("Finding account with roles by email: {}", email);
        Account account = accountRepository.findWithRolesByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account with email: " + email + " not found"));

        log.debug("Checking if account disabled.");
        if (Boolean.FALSE.equals(account.getEnabled())) {
            throw new DisabledException("Your account is disabled");
        }

        log.debug("Returning AccountDetails.");
        return new AccountDetails(account);
    }
}
