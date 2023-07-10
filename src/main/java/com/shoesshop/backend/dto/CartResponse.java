package com.shoesshop.backend.dto;

import java.util.List;

public class CartResponse {
    private int id;
    private int cardId;
    private List<CartItemResponse> listShoes;
    private double totalPrice;
}
