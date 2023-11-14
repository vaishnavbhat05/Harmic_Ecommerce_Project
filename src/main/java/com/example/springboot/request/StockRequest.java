package com.example.springboot.request;

import com.example.springboot.entity.Products;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class StockRequest {

    private Products products;

    private int qty;

    private LocalDate date;
}
