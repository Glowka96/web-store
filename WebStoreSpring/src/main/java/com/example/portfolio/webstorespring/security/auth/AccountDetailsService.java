package com.example.portfolio.webstorespring.security.auth;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "email", email));

        if (Boolean.FALSE.equals(account.getEnabled())) {
            throw new DisabledException("Your account is disabled");
        }

        return new AccountDetails(account);
    }
}
