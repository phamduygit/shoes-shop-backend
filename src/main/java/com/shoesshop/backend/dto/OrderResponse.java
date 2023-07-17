package com.shoesshop.backend.dto;

import com.shoesshop.backend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private int id;
    private String productName;
    private String imageUrl;
    private int size;
    private int quantity;
    private String shippingStatus;
    private double price;
    private double totalPrice;
    private int userId;
    private int rating;
    private String comment;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.productName = order.getShoes().getName();
        this.size = order.getSize();
        this.quantity = order.getQuantity();
        this.shippingStatus = order.getShippingStatus().name();
        this.price = order.getShoes().getPrice();
        this.totalPrice = order.getShoes().getPrice() * order.getQuantity();
        this.userId = order.getUser().getId();
        this.rating = order.getRating();
        this.comment = order.getComment();
        this.imageUrl = order.getShoes().getCoverImage();
    }
}
