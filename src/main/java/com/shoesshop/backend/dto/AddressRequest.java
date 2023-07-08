package com.shoesshop.backend.dto;

import lombok.Data;

@Data
public class AddressRequest {
    private String address;
    private boolean selected;
}
