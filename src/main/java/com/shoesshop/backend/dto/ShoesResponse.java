package com.shoesshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoesResponse {
    private int id;
    private String name;
    private double price;
    private String coverImage;
    private List<String> colors;
    private String status;
    private double priceSales;
}
