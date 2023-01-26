package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
