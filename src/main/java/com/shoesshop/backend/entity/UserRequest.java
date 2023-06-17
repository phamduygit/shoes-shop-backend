package com.shoesshop.backend.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String avatar;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
