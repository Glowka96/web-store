package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
