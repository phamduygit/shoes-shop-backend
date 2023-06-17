package com.shoesshop.backend.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String avatar;
    private String phoneNumber;
    private String email;
}
