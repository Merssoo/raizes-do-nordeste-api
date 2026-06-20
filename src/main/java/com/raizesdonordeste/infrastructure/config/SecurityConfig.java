package com.raizesdonordeste.infrastructure.config;

import com.raizesdonordeste.domain.enums.RoleEnum;
import com.raizesdonordeste.infrastructure.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/error/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/unidades").hasRole(RoleEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/unidades").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/estoques/produtos").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name())
                        .requestMatchers(HttpMethod.GET, "/api/estoques/*/produtos").authenticated()

                        .requestMatchers(HttpMethod.PUT, "/api/estoques/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name())
                        .requestMatchers(HttpMethod.GET, "/api/estoques/unidade/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name(), RoleEnum.ATENDENTE.name())

                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole(RoleEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/create-cliente").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name(), RoleEnum.ATENDENTE.name())
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/create-staff").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name())

                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
