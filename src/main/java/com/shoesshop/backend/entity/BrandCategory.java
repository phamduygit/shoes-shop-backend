package com.shoesshop.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands")
public class BrandCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    private String image;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "brandCategory")
    @JsonIgnore
    private List<Shoes> listShoes;
}
