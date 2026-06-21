package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.RoleDTO;
import com.raizesdonordeste.domain.entity.Role;
import com.raizesdonordeste.infrastructure.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, RoleDTO, Long> {

    public RoleService(RoleRepository repository) {
        super(repository, Role.class);
    }

    @Override
    public Role toEntity(RoleDTO dto) {
        return new Role(dto.getId(), dto.getNome());
    }

    @Override
    public RoleDTO toDto(Role entity) {
        return new RoleDTO(entity.getId(), entity.getNome());
    }
}
