package com.shoesshop.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite", uniqueConstraints={
        @UniqueConstraint(columnNames = {"shoes_id", "user_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "shoes_id")
    Shoes shoes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
