package com.shoesshop.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.shoesshop.backend.entity.Permission.*;
import static com.shoesshop.backend.entity.Role.ADMIN;
import static com.shoesshop.backend.entity.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/shoes/**", "/api/v1/brand-category/**", "/api/v1/promote/**").permitAll()
                .requestMatchers("/api/v1/shoes/**", "/api/v1/brand-category/**", "/api/v1/promote/**").hasAnyRole(ADMIN.name(), USER.name())
                .requestMatchers(HttpMethod.POST, "/api/v1/shoes/**", "/api/v1/brand-category/**", "/api/v1/promote/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(HttpMethod.PUT, "/api/v1/shoes/**", "/api/v1/brand-category/**", "/api/v1/promote/**").hasAnyAuthority(ADMIN_UPDATE.name())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/shoes/**", "/api/v1/brand-category/**", "/api/v1/promote/**").hasAnyAuthority(ADMIN_DELETE.name())
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return httpSecurity.build();
    }
}
