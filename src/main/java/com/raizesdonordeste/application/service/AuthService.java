package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.request.LoginRequest;
import com.raizesdonordeste.api.dto.request.RegisterRequest;
import com.raizesdonordeste.api.dto.response.AuthResponse;
import com.raizesdonordeste.api.exception.BusinessException;
import com.raizesdonordeste.domain.entity.Role;
import com.raizesdonordeste.domain.entity.User;
import com.raizesdonordeste.domain.enums.RoleEnum;
import com.raizesdonordeste.infrastructure.repository.RoleRepository;
import com.raizesdonordeste.infrastructure.repository.UserRepository;
import com.raizesdonordeste.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        Role role = roleRepository
                .findByNome(RoleEnum.CLIENTE.name())
                .orElseThrow(() -> new BusinessException("Role não encontrada"));

        User user = new User();
        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setSenha(passwordEncoder.encode(request.senha()));
        user.setAtivo(true);
        user.setRole(role);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Email ou senha invalido"));

        if (!passwordEncoder.matches(request.senha(), user.getSenha())) {
            throw new BusinessException("Email ou senha invalido");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
