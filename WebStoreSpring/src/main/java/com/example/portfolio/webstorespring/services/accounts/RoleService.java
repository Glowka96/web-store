package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
class RoleService {

    private final RoleRepository roleRepository;

    Set<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    void initializeRole(String roleName) {
        log.debug("Checking if role exist by name: {}",  roleName);
        if (!roleRepository.existsByName(roleName)) {
            log.debug("Role doesn't exist.");
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepository.save(role);
            log.debug("Saved role.");
        }
    }
}
