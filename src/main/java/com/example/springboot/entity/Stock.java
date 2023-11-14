package com.example.springboot.entity;

import com.example.springboot.request.StockRequest;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "stock_tbl")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "prod_id",nullable = false)
    private Products products;

    @Column(name = "quantity",nullable = false)
    @Min(value=0, message = "Quantity must be greater than or equal to 0.")
    private Integer qty;

    @Column(name = "date",nullable = false)
    private LocalDate date;

    public Stock(StockRequest req) {
        this.products=req.getProducts();
        this.qty= req.getQty();
        this.date=req.getDate();
    }
}
