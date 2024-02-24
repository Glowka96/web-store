package com.example.portfolio.webstorespring.services.accounts;

import com.example.portfolio.webstorespring.model.entity.accounts.Role;
import com.example.portfolio.webstorespring.repositories.accounts.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.example.portfolio.webstorespring.buildhelpers.accounts.RoleBuilderHelper.createRole;
import static org.assertj.core.api.Assertions.assertThat;
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
        // given
        Role role = createRole();
        given(roleRepository.findByName(anyString())).willReturn(Set.of(role));

        // when
        Set<Role> result = underTest.findRoleByName("Test");

        // then
        assertThat(result).isEqualTo(Set.of(role));
        verify(roleRepository, times(1)).findByName(anyString());
    }

    @Test
    void shouldInitializeRole() {
        // given
        given(roleRepository.existsByName(anyString())).willReturn(false);

        // when
        underTest.initializeRole("Test");

        // then
        verify(roleRepository, times(1)).existsByName(anyString());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void shouldNoInitializeRoleWhenRoleIsExist() {
        // given
        given(roleRepository.existsByName(anyString())).willReturn(true);

        // when
        underTest.initializeRole("Test");

        // then
        verify(roleRepository, times(1)).existsByName(anyString());
        verifyNoMoreInteractions(roleRepository);
    }
}
