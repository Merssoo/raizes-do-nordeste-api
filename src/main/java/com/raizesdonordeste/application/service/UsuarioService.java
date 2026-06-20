package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.request.LoginRequest;
import com.raizesdonordeste.api.dto.request.RegisterRequest;
import com.raizesdonordeste.api.dto.response.AuthResponse;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.domain.entity.Role;
import com.raizesdonordeste.domain.entity.Usuario;
import com.raizesdonordeste.domain.enums.RoleEnum;
import com.raizesdonordeste.infrastructure.repository.RoleRepository;
import com.raizesdonordeste.infrastructure.repository.UsuarioRepository;
import com.raizesdonordeste.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        Role role = roleRepository
                .findByNome(RoleEnum.CLIENTE.name())
                .orElseThrow(() -> new BusinessException("Role não encontrada"));

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setAtivo(true);
        usuario.setRole(role);

        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Email ou senha invalido"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BusinessException("Email ou senha invalido");
        }

        String token = jwtService.generateToken(usuario);

        return new AuthResponse(token);
    }
}
