package com.shoesshop.backend.dto;

import com.shoesshop.backend.entity.BrandCategory;
import com.shoesshop.backend.entity.Shoes;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShoesDetailResponse {
    private int shoesId;
    private String name;
    private String coverImage;
    private double price;
    private List<String> colors;
    private List<String> sizes;
    private String description;
    private Shoes.Status status;
    private double priceSales;
    private BrandCategory brandCategory;
    private boolean favorite;

    public ShoesDetailResponse(Shoes shoes, boolean favorite) {
        this.shoesId = shoes.getShoesId();
        this.name = shoes.getName();
        this.coverImage = shoes.getCoverImage();
        this.price = shoes.getPrice();
        this.colors = shoes.getColors();
        this.sizes = shoes.getSizes();
        this.description = shoes.getDescription();
        this.status = shoes.getStatus();
        this.priceSales = shoes.getPriceSales();
        this.brandCategory = shoes.getBrandCategory();
        this.favorite = favorite;
    }
}
