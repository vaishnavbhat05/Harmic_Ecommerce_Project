package com.example.springboot.repository;

import com.example.springboot.entity.Products;
import com.example.springboot.entity.Stock;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock,Long> {

    @Query("SELECT qty FROM Stock s WHERE s.products=:prod_id")
    public int checkAvailability(@Param("prod_id") Products prod_id);

    @Query("SELECT s.products FROM Stock s WHERE DATEDIFF(:cur_date,s.date) between 0 and 2")
    public List<Products> newArrivals(@Param("cur_date") LocalDate cur_date);

    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Transactional
    @Query("update Stock s set s.qty=:qty where s.products=:prodId")
    public void updateQty(@Param("prodId") Products prodId, @Param("qty") int qty);

    public Stock findByProducts(Products products);

}
