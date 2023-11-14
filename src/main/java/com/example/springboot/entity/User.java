package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.List;

/*** Vaishnav Bhat */

@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "user_tbl")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "fname",nullable = false)
    private String firstName;

    @Column(name = "lname",nullable = false)
    private String lastName;

    @Column(name = "email_id", unique = true,nullable = false)
    @Email
    private String email;

    @Column(name = "password",nullable = false)
    @Size(min=6)
    private String password;

    @Column(name = "ph_no", unique = true,nullable = false)
    @Size(min=10)
    private String phone;

    @Column(name = "role",nullable = false)
    private String role;

    @Column(name = "reset_token")
    @JsonIgnore
    private String resetToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference(value = "cart-user")
    private List<Cart> cart;

    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference(value = "checkout-user")
    private List<Checkout> checkout;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference(value = "wishlist-user")
    private List<Wishlist> wishlist;
}

