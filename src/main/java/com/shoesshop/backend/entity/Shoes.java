package com.shoesshop.backend.entity;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    private Status Status;

    public enum Status {
        NEW, SALE, NONE
    }

    private double priceSales;

    @ManyToOne
    @JoinColumn(name = "brands_id")
    private BrandCategory brandCategory;

}
