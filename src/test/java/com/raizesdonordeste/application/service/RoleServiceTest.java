package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.RoleDTO;
import com.raizesdonordeste.domain.entity.Role;
import com.raizesdonordeste.infrastructure.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void toEntity_ShouldConvertRoleDTOToRole() {
        RoleDTO dto = new RoleDTO(1L, "ADMIN");
        Role entity = roleService.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNome(), entity.getNome());
    }

    @Test
    void toDto_ShouldConvertRoleToRoleDTO() {
        Role entity = new Role(1L, "USER");
        RoleDTO dto = roleService.toDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getNome(), dto.getNome());
    }
}
