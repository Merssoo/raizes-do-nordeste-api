package com.raizesdonordeste.application.service;

import com.raizesdonordeste.api.dto.request.CreateClienteRequest;
import com.raizesdonordeste.api.dto.request.CreateStaffRequest;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final String adminSecretKey;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          @Value("${app.admin-secret-key}") String adminSecretKey) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.adminSecretKey = adminSecretKey;
    }

    @Transactional
    public void criarCliente(CreateClienteRequest request) {
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

    @Transactional
    public void criarUsuarioStaff(CreateStaffRequest request, Usuario criador) {
        RoleEnum roleCriador = RoleEnum.valueOf(criador.getRole().getNome());

        if (roleCriador.equals(RoleEnum.CLIENTE)) {
            throw new BusinessException("Usuário não tem permissão para criar staff");
        }

        if (roleCriador == RoleEnum.GERENTE) {
            if (request.role() != RoleEnum.COZINHA && request.role() != RoleEnum.ATENDENTE) {
                throw new BusinessException("GERENTE só pode criar COZINHA ou ATENDENTE");
            }
        }

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        Role role = roleRepository
                .findByNome(request.role().name())
                .orElseThrow(() -> new BusinessException("Role não encontrada"));

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setAtivo(true);
        usuario.setRole(role);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void registerAdmin(RegisterRequest request, String secretKey) {
        if (!adminSecretKey.equals(secretKey)) {
            throw new BusinessException("Chave de segurança inválida.");
        }
        
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        Role role = roleRepository
                .findByNome(RoleEnum.ADMIN.name())
                .orElseThrow(() -> new BusinessException("Role não encontrada"));

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setAtivo(true);
        usuario.setRole(role);

        usuarioRepository.save(usuario);
    }

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
