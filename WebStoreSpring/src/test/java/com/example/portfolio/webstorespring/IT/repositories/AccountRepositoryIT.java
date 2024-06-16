package com.example.portfolio.webstorespring.IT.repositories;

import com.example.portfolio.webstorespring.IT.ContainersConfig;
import com.example.portfolio.webstorespring.buildhelpers.accounts.AccountBuilderHelper;
import com.example.portfolio.webstorespring.buildhelpers.accounts.RoleBuilderHelper;
import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.AccountAddressRepository;
import com.example.portfolio.webstorespring.repositories.accounts.AccountRepository;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

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
    private AccountAddressRepository accountAddressRepository;
    @Autowired
    EntityManager entityManager;

    private static final String email = "test@test.pl";

    @BeforeEach
    void init() {
        roleRepository.deleteAll();
        Role role = RoleBuilderHelper.createUserRole();
        Role savedRole = roleRepository.save(role);

        accountRepository.deleteAll();
        accountAddressRepository.deleteAll();
        Account account = AccountBuilderHelper.createAccountWithRoleUserAndAccountAddress(savedRole);
        accountRepository.saveAndFlush(account);
    }

    @Test
    void shouldFindAccountByEmailWithRoleAndWithoutAddress() {
        entityManager.clear();

        Optional<Account> account = accountRepository.findByEmail(email);

        assertThat(account).isPresent();
        assertThat(account.get().getEmail()).isEqualTo(email);

        boolean rolesInitialized = Hibernate.isInitialized(account.get().getRoles());
        assertThat(rolesInitialized).isFalse();

        boolean addressInitialized = Hibernate.isInitialized(account.get().getAddress());
        assertThat(addressInitialized).isFalse();
    }

    @Test
    @Transactional
    void shouldFindAccountByEmailWithRoleAndAddress() {
        Optional<Account> account = accountRepository.findAccountWithRolesAndAddressByEmail(email);

        assertThat(account).isNotNull();
        assertThat(account.get().getEmail()).isEqualTo(email);
        assertThat(account.get().getRoles()).isNotNull();
        assertThat(account.get().getAddress()).isNotNull();
    }
}
