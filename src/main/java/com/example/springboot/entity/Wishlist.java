package com.example.springboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

/*** Vaishnav Bhat */

@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "wishlist_tbl")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "prod_id",nullable = false)
    private Products products;
}
