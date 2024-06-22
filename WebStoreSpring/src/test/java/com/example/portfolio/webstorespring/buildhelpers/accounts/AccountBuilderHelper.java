package com.example.portfolio.webstorespring.buildhelpers.accounts;

import com.example.portfolio.webstorespring.model.dto.accounts.request.AccountRequest;
import com.example.portfolio.webstorespring.model.dto.accounts.response.AccountResponse;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import java.util.Set;

public class AccountBuilderHelper {

    public static final Property<Account, Long> ID = new Property<>();
    public static final Property<Account, String> FIRST_NAME = new Property<>();
    public static final Property<Account, String> LAST_NAME = new Property<>();
    public static final Property<Account, String> EMAIL = new Property<>();
    public static final Property<Account, String> PASSWORD = new Property<>();
    public static final Property<Account, Boolean> ENABLED = new Property<>();
    public static final Property<Account, Set<Role>> ROLES = new Property<>();

    private static final String ACCOUNT_FIRST_NAME = "Name";
    private static final String ACCOUNT_LAST_NAME = "Lastname";
    private static final String ACCOUNT_EMAIL = "test@test.pl";
    private static final String ACCOUNT_PASSWORD = "Password123*";
    private static final String ACCOUNT_IMAGE_URL = "test.pl/test.png";
    private static final Role ROLE_USER = RoleBuilderHelper.createUserRole();

    public static final Instantiator<Account> BASIC_ACCOUNT = lookup ->
            Account.builder()
            .id(lookup.valueOf(ID, 1L))
            .firstName(lookup.valueOf(FIRST_NAME, ACCOUNT_FIRST_NAME))
            .lastName(lookup.valueOf(LAST_NAME, ACCOUNT_LAST_NAME))
            .email(lookup.valueOf(EMAIL, ACCOUNT_EMAIL))
            .password(lookup.valueOf(PASSWORD, ACCOUNT_PASSWORD))
            .enabled(lookup.valueOf(ENABLED, Boolean.TRUE))
            .roles(lookup.valueOf(ROLES, Set.of(ROLE_USER)))
            .imageUrl(ACCOUNT_IMAGE_URL)
            .build();

    public static AccountRequest createAccountRequest() {
        return AccountRequest.builder()
                .firstName(ACCOUNT_FIRST_NAME)
                .lastName(ACCOUNT_LAST_NAME)
                .password(ACCOUNT_PASSWORD)
                .build();
    }

    public static AccountResponse createAccountResponse() {
        return AccountResponse.builder()
                .id(1L)
                .firstName(ACCOUNT_FIRST_NAME)
                .lastName(ACCOUNT_LAST_NAME)
                .email(ACCOUNT_EMAIL)
                .imageUrl(ACCOUNT_IMAGE_URL)
                .build();
    }
}
