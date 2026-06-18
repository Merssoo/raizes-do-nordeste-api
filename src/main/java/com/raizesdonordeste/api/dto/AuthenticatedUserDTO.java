package com.raizesdonordeste.api.dto;

public record AuthenticatedUserDTO(
        Long id,
        String email,
        String nome,
        String role
) {}
