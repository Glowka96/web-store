package com.example.porfolio.webstorespring.services.auth;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return repository.findByEmail(email)
                .map(AccountDetails::new)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", email));
    }

}
