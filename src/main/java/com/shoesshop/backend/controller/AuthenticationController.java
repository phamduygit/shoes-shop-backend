package com.shoesshop.backend.controller;

import com.shoesshop.backend.dto.AuthenticationRequest;
import com.shoesshop.backend.dto.AuthenticationResponse;
import com.shoesshop.backend.dto.RegisterRequest;
import com.shoesshop.backend.exception.AuthErrorException;
import com.shoesshop.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Log4j2
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        AuthenticationResponse authenticationResponse =authenticationService.refreshToken(request, response);
        if (authenticationResponse == null) {
            throw new AuthErrorException("Invalid refresh token");
        }
        return ResponseEntity.ok(authenticationService.refreshToken(request, response));
    }

    @PostMapping(value ="/login-with-google")
    public ResponseEntity<AuthenticationResponse> loginWithGoogle(@RequestBody Map<String, String> request) {
        log.info("Email: " + request.get("email"));
        return ResponseEntity.ok(authenticationService.authenticateWithGoogle(request.get("email")));
    }

    @PostMapping("/register-with-google")
    public ResponseEntity<AuthenticationResponse> registerWithGoogle(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
}
