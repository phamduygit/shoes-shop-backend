package com.shoesshop.backend.dto;

import com.shoesshop.backend.entity.Shoes;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ShoesRequest {
    private int id;

    private String name;

    private String coverImage;

    private double price;

    private List<String> colors;

    private List<String> sizes;

    private String description;

    private Shoes.Status status;

    private double priceSales;

    private int brandId;
}
