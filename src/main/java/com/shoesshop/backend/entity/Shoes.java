package com.shoesshop.backend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shoes")
@Entity
@JsonPropertyOrder({ "shoesId", "name", "coverImage" })
public class Shoes extends BaseEntity { 
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int shoesId;

    @NotBlank(message = "Name must be not blank")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    @Column(columnDefinition = "VARCHAR(2048) NOT NULL, FULLTEXT KEY full_text_index (name)")
    private String name;

    @NotBlank(message = "Cover image must be not blank")
    private String coverImage;

    @Min(value = 1, message = "Value of price must be at least 1$")
    private double price;

    private List<String> colors;

    private List<String> sizes;

    @Column(length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        NEW, SALE, NONE
    }

    private double priceSales;

    @ManyToOne
    @JoinColumn(name = "brands_id")
    private BrandCategory brandCategory;

    @OneToMany(mappedBy = "shoes", fetch = FetchType.LAZY)
    private Set<Favorite> favoriteSet;

    @OneToMany(mappedBy = "shoes", fetch = FetchType.LAZY)
    private Set<CartItem> cartItems;

    @OneToMany(mappedBy = "shoes", fetch = FetchType.LAZY)
    private Set<Order> orders;
}
