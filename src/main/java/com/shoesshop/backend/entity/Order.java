package com.shoesshop.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String address;

    @ManyToOne
    @JoinColumn(name = "shoes_id")
    private Shoes shoes;

    private int size;

    private int quantity;

    private int rating;

    private String comment;

    @Enumerated(EnumType.STRING)
    private ShippingMethod shippingMethod;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    public enum ShippingStatus {
        PREPARE, IN_DELIVERY, WAIT_FOR_RECEIVE, COMPLETE
    }
    public enum PaymentMethod {
        CASH, PAYPAL, GOOGLE_PAY, CARD
    }

    public enum ShippingMethod {
        NORMAL, FAST, EXPRESS
    }
}
