package com.raizesdonordeste.infrastructure.security;

import com.raizesdonordeste.domain.entity.Role;
import com.raizesdonordeste.domain.entity.Usuario;
import com.raizesdonordeste.domain.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "defaultSecretKeyWithAtLeast32CharactersLong12345678901234567890");
    }

    @Test
    void shouldGenerateAndValidateToken() {
        Usuario user = new Usuario();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setNome("Test User");
        Role role = new Role();
        role.setNome(RoleEnum.ADMIN.name());
        user.setRole(role);

        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));

        Claims claims = jwtService.getClaimsFromToken(token);
        assertEquals("test@test.com", claims.getSubject());
        assertEquals(1, ((Number) claims.get("userId")).longValue());
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        assertFalse(jwtService.isTokenValid("invalid.token.here"));
    }
}
