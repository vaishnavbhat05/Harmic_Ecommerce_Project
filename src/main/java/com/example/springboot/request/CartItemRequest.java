package com.example.springboot.request;

import java.math.BigDecimal;

import com.example.springboot.entity.Cart;
import com.example.springboot.entity.Products;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemRequest {

    private Cart cart;

    private Products products;

    private int qty;

    private BigDecimal total;

}
