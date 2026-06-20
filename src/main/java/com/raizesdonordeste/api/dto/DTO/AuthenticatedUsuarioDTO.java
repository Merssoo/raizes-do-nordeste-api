package com.raizesdonordeste.api.dto.DTO;

public record AuthenticatedUsuarioDTO(Long id,
                                      String email,
                                      String nome,
                                      String role) {
}
