package com.shoesshop.backend.controller;

import com.shoesshop.backend.dto.JwtResponse;
import com.shoesshop.backend.dto.UserRequest;
import com.shoesshop.backend.dto.UserResponse;
import com.shoesshop.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @GetMapping("/check-token")
    public ResponseEntity<JwtResponse> isValidToken() {
        return ResponseEntity.ok(JwtResponse.builder().isValid(true).message("Valid token").status(HttpStatus.OK).build());
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getUserInfo() {
        log.info("/api/v1/user get");
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PutMapping()
    public ResponseEntity<UserResponse> updateUserInfo(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUserInfo(userRequest));
    }

}
