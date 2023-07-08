package com.shoesshop.backend.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private boolean isValid;
    private String message;
    private HttpStatus status;
}
