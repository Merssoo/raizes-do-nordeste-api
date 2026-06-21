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
                        .requestMatchers("/auth/**", "/error/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/unidades").hasRole(RoleEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/unidades").authenticated()

                        .requestMatchers(HttpMethod.POST, "/estoques/produtos").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name())
                        .requestMatchers(HttpMethod.GET, "/estoques/produtos").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name())
                        .requestMatchers(HttpMethod.GET, "/estoques/*/produtos").authenticated()

                        .requestMatchers(HttpMethod.PUT, "/estoques/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name())
                        .requestMatchers(HttpMethod.GET, "/estoques/unidade/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name(), RoleEnum.ATENDENTE.name())

                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole(RoleEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/usuarios/create-cliente").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name(), RoleEnum.ATENDENTE.name())
                        .requestMatchers(HttpMethod.POST, "/usuarios/create-staff").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.GERENTE.name())

                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
