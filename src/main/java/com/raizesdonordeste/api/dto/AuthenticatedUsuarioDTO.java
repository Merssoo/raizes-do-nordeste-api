package com.raizesdonordeste.api.dto;

public record AuthenticatedUsuarioDTO(Long id,
                                      String email,
                                      String nome,
                                      String role) {
}
