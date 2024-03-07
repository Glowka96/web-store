package com.example.portfolio.webstorespring.repositories.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Set<Role> findByName(String name);

    boolean existsByName(String name);
}
