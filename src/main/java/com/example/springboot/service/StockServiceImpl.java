package com.example.springboot.service;

import com.example.springboot.entity.Products;
import com.example.springboot.entity.Stock;
import com.example.springboot.repository.StockRepository;
import com.example.springboot.repository.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class StockServiceImpl implements StockService{
    private StockRepository stockRepository;
    private ProductRepository productRepository;

    public StockServiceImpl(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public Stock addStock(Stock newStock) {
        return stockRepository.save(newStock);
    }

    @Override
    public Stock findById(Long Stock_id) {
        return stockRepository.findById(Stock_id).orElse(null);
    }

    @Override
    public void deleteStockById(Long Stock_id) {
        stockRepository.deleteById(Stock_id);
    }

    @Override
    public void deleteAll() {
        stockRepository.deleteAll();
    }

    @Override
    public int availability(Products prod_id){

        return stockRepository.checkAvailability(prod_id);
    }

    @Override
    public List<Products> newArrivals(){
        LocalDate cur_date=LocalDate.now();
        return stockRepository.newArrivals(cur_date);
    }

    @Override
    public void updateStock(Products prod_id,int qty){
        stockRepository.updateQty(prod_id,qty);
    }

    @Override
    public Stock updateStock(Long id, Stock updatedStock) {
        Optional<Stock> Stock = stockRepository.findById(id);
        if (Stock.isPresent()) {
            Stock existingStock = Stock.get();
            if (updatedStock.getQty() != null) {
                existingStock.setQty(updatedStock.getQty());
                if(existingStock.getQty()==0){
                    productRepository.setAvailability("Out of stock",existingStock.getProducts().getId());
                }
                else{
                    productRepository.setAvailability("In stock",existingStock.getProducts().getId());
                }
            }
            if (updatedStock.getProducts() != null) {
                existingStock.setProducts(updatedStock.getProducts());
            }
            if (updatedStock.getDate() != null) {
                existingStock.setDate(updatedStock.getDate());
            }
            return stockRepository.save(existingStock);
        }
        else {
            return null;
        }
    }
}
