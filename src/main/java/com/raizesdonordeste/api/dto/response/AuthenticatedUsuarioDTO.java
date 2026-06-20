package com.raizesdonordeste.api.dto.response;

public record AuthenticatedUsuarioDTO(Long id,
                                      String email,
                                      String nome,
                                      String role) {
}
