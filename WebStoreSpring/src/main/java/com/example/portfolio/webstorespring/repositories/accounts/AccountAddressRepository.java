package com.example.portfolio.webstorespring.repositories.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.AccountAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountAddressRepository extends JpaRepository<AccountAddress, Long> {
    Optional<AccountAddress> findAccountAddressByAccount_Id(Long account_id);
}
