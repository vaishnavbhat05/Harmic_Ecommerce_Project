package com.example.springboot.service;

import com.example.springboot.entity.Products;

import java.math.BigDecimal;
import java.util.Comparator;

public class ProductPriceComparator implements Comparator<Products> {
    @Override
    public int compare(Products p1, Products p2) {
        BigDecimal price1 = p1.getPrice();
        BigDecimal price2 = p2.getPrice();
        return price1.compareTo(price2);
    }
}
