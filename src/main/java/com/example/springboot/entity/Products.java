package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

/*** Vaishnav Bhat */

@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "product_tbl")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_id")
    private Long id;

    @Column(name = "prodName", unique = true,nullable = false)
    private String prodName;

    @ManyToOne
    @JoinColumn(name = "cat_id",nullable = false)
    private Category category;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "description",nullable = false)
    private String desc;

    @Column(name = "prod_img",nullable = false)
    private String image;

    @Column(name = "availability")
    private String avail;

    @JsonIgnore
    @OneToMany(mappedBy = "products", cascade = CascadeType.REMOVE)
    private List<CartItem> cartItem;

    @JsonIgnore
    @OneToMany(mappedBy = "products", cascade = CascadeType.REMOVE)
    private List<Wishlist> wishlist;

    @JsonIgnore
    @OneToOne(mappedBy = "products", cascade = CascadeType.REMOVE)
    private Stock stock;

}
