package com.example.springboot.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CheckoutResponseDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private int totalQuantity;
    private BigDecimal totalCost;

    private String address;
    private String payment;
    private LocalDate date;
    private LocalTime time;

    public CheckoutResponseDTO(String firstName, String lastName, String phone, int totalQuantity, BigDecimal totalCost,
                               String address, String payment, LocalDate date, LocalTime time) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;
        this.address = address;
        this.payment = payment;
        this.date = date;
        this.time = time;
    }

}
