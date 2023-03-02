package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.AccountCanNotModifiedException;
import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import com.example.porfolio.webstorespring.model.entity.accounts.AccountRoles;
import com.example.porfolio.webstorespring.services.auth.AccountDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AuthorityService {
    protected void validateAuthorityLoggedUser(Account foundAccount, String option) {
        AccountDetails principal = getPrincipal();

        String loggedUsername = principal.getUsername();

        String authorityName = getNameAuthority(principal);

        if (authorityName != null &&
                authorityName.equalsIgnoreCase(AccountRoles.USER.name()) &&
                !loggedUsername.equalsIgnoreCase(foundAccount.getEmail())) {
            throw new AccountCanNotModifiedException(option);
        }
    }

    private AccountDetails getPrincipal() {
        return (AccountDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private String getNameAuthority(AccountDetails principal) {
        return principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }
}
