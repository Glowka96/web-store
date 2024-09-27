package com.example.portfolio.webstorespring.repositories.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Query("""
        SELECT a FROM Account a
        LEFT JOIN FETCH a.roles
        WHERE a.email = :email
""")
    Optional<Account> findAccountWithRolesByEmail(@Param("email") String email);

    Boolean existsByEmail(String email);
}
