package com.example.springboot.service;

import com.example.springboot.entity.Cart;
import com.example.springboot.entity.CartItem;
import com.example.springboot.entity.Products;

import java.math.BigDecimal;
import java.util.List;
public interface CartItemService {
    List<CartItem> getAllCartItem();
    CartItem addItem(CartItem newCartItem);
    CartItem findById(Long cartitem_id);
    void deleteCartItemById(Long cartitem_id);
    void deleteAll();
    List<CartItem> getCartitemByCartId(Long cartId);
    CartItem findByProduct(Products products, Cart cart);
    CartItem updateItem(Long cartItemId,int qty);
    BigDecimal calcTotal(BigDecimal price, int qty);
    String updateQty(Long id, CartItem updatedQty);
}
