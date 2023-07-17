package com.shoesshop.backend.dto;

import com.shoesshop.backend.entity.Shoes;
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
    private boolean favorite;

    public void setValue(Shoes shoes) {
        this.id = shoes.getShoesId();
        this.name = shoes.getName();
        this.price = shoes.getPrice();
        this.coverImage = shoes.getCoverImage();
        this.colors = shoes.getColors();
        this.status = shoes.getStatus().name();
        this.priceSales = shoes.getPriceSales();
        this.favorite = false;
    }

    public void setValue(Shoes shoes, boolean favorite) {
        this.id = shoes.getShoesId();
        this.name = shoes.getName();
        this.price = shoes.getPrice();
        this.coverImage = shoes.getCoverImage();
        this.colors = shoes.getColors();
        this.status = shoes.getStatus().name();
        this.priceSales = shoes.getPriceSales();
        this.favorite = favorite;
    }
}
