package com.raizesdonordeste.api.dto.response;

public record AuthResponse(
        String token,
        String tipo
) {
    public AuthResponse(String token) {
        this(token, "Bearer");
    }
}
