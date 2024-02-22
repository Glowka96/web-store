package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
class RoleService {

    private final RoleRepository roleRepository;

    Set<Role> findRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
