package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.RoleBuilderHelper.createUserRole;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService underTest;

    @Test
    void shouldFindRoleByName() {
        Role role = createUserRole();
        given(roleRepository.findByName(anyString())).willReturn(Set.of(role));

        Set<Role> result = underTest.findByName("Test");

        assertEquals(Set.of(role), result);
        verify(roleRepository, times(1)).findByName(anyString());
    }

    @Test
    void shouldInitializeRole_whenRolesNotExist() {
        given(roleRepository.existsByName(anyString())).willReturn(false);

        underTest.initializeRole("Test");

        verify(roleRepository, times(1)).existsByName(anyString());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void shouldNoInitializeRole_whenRoleIsExist() {
        given(roleRepository.existsByName(anyString())).willReturn(true);

        underTest.initializeRole("Test");

        verify(roleRepository, times(1)).existsByName(anyString());
        verifyNoMoreInteractions(roleRepository);
    }
}
