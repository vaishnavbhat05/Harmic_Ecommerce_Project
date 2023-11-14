package com.example.springboot.entity;

import com.example.springboot.request.CartRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/*** Vaishnav Bhat */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart_tbl")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Checkout> checkouts;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CartItem> cartItem;

    public Cart(CartRequest req) {
        this.user =req.getUser();
        this.cartItem=req.getCartItem();
    }

    public Cart(User user) {
        this.user = user;
    }

    @JsonIgnore
    public List<CartItem> getCartItems() {
        if (cartItem == null) {
            cartItem = new ArrayList<>();
        }
        return cartItem;
    }

}
