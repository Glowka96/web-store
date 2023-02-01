package com.example.porfolio.webstorespring.repositories;

import com.example.porfolio.webstorespring.model.entity.accounts.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
}
