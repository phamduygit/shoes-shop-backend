package com.shoesshop.backend.dto;

import lombok.Data;

@Data
public class FavoriteRequest {
    private int userId;
    private int shoesId;
}
