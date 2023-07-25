package com.shoesshop.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "promote")
public class Promote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String color;
    private double discountValue;
    private String title;
    private String description;
    private String coverImage;
    private int quantity;
    private Date startDate;
    private Date endDate;
}
