package com.example.portfolio.webstorespring.repositories.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @EntityGraph(value = "account-with-roles-and-address-entity-graph")
    Optional<Account> findByEmailWithRolesAndAddress(String email);

    Boolean existsByEmail(String email);
}
