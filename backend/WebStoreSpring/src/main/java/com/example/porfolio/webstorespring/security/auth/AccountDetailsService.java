package com.example.porfolio.webstorespring.security.auth;

import com.example.porfolio.webstorespring.exceptions.AccountCanNotModifiedException;
import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.repositories.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return repository.findByEmail(email)
                .map(AccountDetails::new)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "email", email));
    }

    public boolean isValidAuthLoggedUser(Long id) {
        String emailAccount = getAuthName();

        Account foundAccount = findAccountByEmail(emailAccount);

        if (!foundAccount.getId().equals(id)) {
            throw new AccountCanNotModifiedException();
        }
        return true;
    }

    private String getAuthName() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    private Account findAccountByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "email", email));
    }
}
