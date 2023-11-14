package com.example.springboot.service;

import com.example.springboot.entity.Products;
import com.example.springboot.entity.Stock;

import java.util.List;
public interface StockService {
    List<Stock> getAllStock();
    Stock addStock(Stock newStock);
    Stock findById(Long Stock_id);
    void deleteStockById(Long stock_id);
    void deleteAll();
    int availability(Products prod_id);
    void updateStock(Products prod_id,int qty);
    List<Products> newArrivals();

    Stock updateStock(Long id, Stock updatedStock);

}
