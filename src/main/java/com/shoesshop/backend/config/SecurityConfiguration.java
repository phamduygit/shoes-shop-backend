package com.shoesshop.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.shoesshop.backend.entity.Permission.*;
import static com.shoesshop.backend.entity.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/api/v1/auth/**").permitAll()

                                .requestMatchers("/api/v1/favorite/**").hasRole(USER.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/favorite/**").hasAuthority(USER_READ.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/favorite/**").hasAnyAuthority(USER_CREATE.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/favorite/**").hasAnyAuthority(USER_DELETE.name())

                                .requestMatchers(HttpMethod.GET, "/api/v1/shoes/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/favorite/**").hasAnyAuthority(ADMIN_CREATE.name())
                                .requestMatchers(HttpMethod.PUT, "/api/v1/favorite/**").hasAnyAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/favorite/**").hasAnyAuthority(ADMIN_DELETE.name())

                                .requestMatchers("/api/v1/address/**").hasRole(USER.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/address/**").hasAuthority(USER_READ.name())
                                .requestMatchers(HttpMethod.PUT, "/api/v1/address/**").hasAuthority(USER_UPDATE.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/address/**").hasAnyAuthority(USER_CREATE.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/address/**").hasAnyAuthority(USER_DELETE.name())

                                .anyRequest().authenticated()

                )
                .sessionManagement(a -> a.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout").addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) -> SecurityContextHolder.clearContext()));

        return httpSecurity.build();
    }
}
