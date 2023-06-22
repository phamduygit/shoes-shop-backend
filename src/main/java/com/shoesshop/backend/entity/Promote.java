package com.shoesshop.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @GeneratedValue
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
