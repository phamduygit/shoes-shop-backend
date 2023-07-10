package com.shoesshop.backend.dto;

import lombok.Data;

@Data
public class AddressRequest {
    private String addressName;
    private String addressDetail;
    private boolean selected;
}
