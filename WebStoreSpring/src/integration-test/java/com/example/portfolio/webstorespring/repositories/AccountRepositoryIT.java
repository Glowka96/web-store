package com.example.portfolio.webstorespring.repositories;

import com.example.portfolio.webstorespring.ContainersConfig;
import com.example.portfolio.webstorespring.buildhelpers.accounts.RoleBuilderHelper;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import com.natpryce.makeiteasy.Maker;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.BASIC_ACCOUNT;
import static com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper.ROLES;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.assertj.core.api.Assertions.assertThat;

@Import(ContainersConfig.class)
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryIT {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EntityManager entityManager;

    private static final String EMAIL = "test@test.pl";

    @BeforeEach
    void init() {
        Role role = RoleBuilderHelper.createUserRole();
        Role savedRole = roleRepository.save(role);

        Maker<Account> accountMaker = a(BASIC_ACCOUNT);
        Account account = make(accountMaker.but(with(ROLES, Set.of(savedRole))));
        accountRepository.save(account);
    }

    @Test
    void shouldFindAccountByEmailWithoutRole() {
        entityManager.clear();

        Optional<Account> account = accountRepository.findByEmail(EMAIL);

        assertThat(account).isPresent();
        assertThat(account.get().getEmail()).isEqualTo(EMAIL);

        boolean rolesInitialized = Hibernate.isInitialized(account.get().getRoles());
        assertThat(rolesInitialized).isFalse();
    }

    @Test
    void shouldFindAccountByEmailWithRole() {
        Optional<Account> account = accountRepository.findAccountWithRolesByEmail(EMAIL);

        assertThat(account).isPresent();
        assertThat(account.get().getEmail()).isEqualTo(EMAIL);
        assertThat(account.get().getRoles()).isNotNull();
    }
}
