package com.shoesshop.backend.config;

import com.google.gson.Gson;
import com.shoesshop.backend.dto.JwtResponse;
import com.shoesshop.backend.entity.Token;
import com.shoesshop.backend.repository.TokenRepository;
import com.shoesshop.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final TokenRepository tokenRepository;

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        boolean isGetMethod = false;
//        boolean isContainApprovedPath = false;
//        String path = request.getRequestURI();
//        isGetMethod = (Objects.equals(request.getMethod(), "GET"));
//
//        List<String> approvedPath = Arrays.asList("/api/v1/shoes", "/api/v1/brand-category", "/api/v1/promote");
//        for (String item : approvedPath) {
//            isContainApprovedPath = isContainApprovedPath | path.contains(item);
//        }
//        return isGetMethod && isContainApprovedPath;
//    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        List<String> approvedPath = Arrays.asList("/api/v1/auth", "/api/v1/shoes", "/api/v1/brand-category", "/api/v1/promote");

        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");

        log.info("Header: " + authHeader);
        final String jwt;
        String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            for (String item : approvedPath) {
                if (request.getServletPath().contains(item)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            throwFilterSecurityException(response, "Unauthorized header");
            return;
        }

        jwt = authHeader.substring(7);
        // Get user email from JWT
        userEmail = "";
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception exception) {
            throwFilterSecurityException(response, exception.getMessage());
            return;
        }

        // Check if user mail exists but user does not authentication yet
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get user object by load username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            } else {
                throwFilterSecurityException(response, "Invalid token!");
            }
        }

    }

    private void throwFilterSecurityException(HttpServletResponse response, String message) throws IOException {
        Gson gson = new Gson();
        log.info("Error: " + message);

        // Create ErrorResponse object
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setMessage(message);
        jwtResponse.setStatus(HttpStatus.UNAUTHORIZED);
        jwtResponse.setValid(false);

        // Create json response
        String jsonResponse = gson.toJson(jwtResponse);

        // Return response object
        PrintWriter out = response.getWriter();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonResponse);
        out.flush();
    }
}
