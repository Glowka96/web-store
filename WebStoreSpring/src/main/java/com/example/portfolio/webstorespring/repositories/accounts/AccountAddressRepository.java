package com.example.portfolio.webstorespring.repositories.accounts;

import com.example.portfolio.webstorespring.models.entity.accounts.AccountAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAddressRepository extends JpaRepository<AccountAddress, Long> {
}
