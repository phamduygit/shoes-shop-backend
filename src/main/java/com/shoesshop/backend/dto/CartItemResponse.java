package com.shoesshop.backend.dto;

import com.shoesshop.backend.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private int itemId;
    private ShoesResponse shoes;
    private int quantity;

    public void setValue(CartItem item) {
        this.itemId = item.getId();
        ShoesResponse tempShoes = new ShoesResponse();
        tempShoes.setValue(item.getShoes());
        shoes = tempShoes;
        this.quantity = item.getQuantity();
    }
}
