package com.example.springboot.entity;

import com.example.springboot.request.CartItemRequest;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "cartitem_tbl")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartitem_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id",nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "prod_id",nullable = false)
    private Products products;

    @Column(name = "quantity",nullable = false)
    @Min(value=1, message = "Quantity must be greater than or equal to 1.")
    private Integer qty;

    @Column(name = "total")
    private BigDecimal total;

    public CartItem(CartItemRequest req) {
        this.cart=req.getCart();
        this.products=req.getProducts();
        this.qty= req.getQty();
        this.total=req.getTotal();
    }
}
