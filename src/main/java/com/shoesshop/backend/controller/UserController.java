package com.shoesshop.backend.controller;

import com.shoesshop.backend.entity.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {


    @GetMapping("/check-token")
    public ResponseEntity<JwtResponse> isValidToken() {
        return ResponseEntity.ok(JwtResponse.builder().isValid(true).message("Valid token").status(HttpStatus.OK).build());
    }

}
