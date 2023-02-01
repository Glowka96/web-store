package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.accounts.AccountAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAddressRepository extends JpaRepository<AccountAddress, Long> {
}
