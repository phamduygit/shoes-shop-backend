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
public class OrderRequest {
    private int cartId;
    private String address;
    private Order.PaymentMethod paymentMethod;
    private Order.ShippingMethod shippingMethod;
}
