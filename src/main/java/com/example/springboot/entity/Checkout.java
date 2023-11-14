package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checkout_tbl")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Checkout {
    public static final BigDecimal FIXED_DELIVERY_CHARGE = BigDecimal.valueOf(50);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private Long id;
    @Column(name = "address")
    private String address;
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @Column(name = "modeOfPayment")
    private String payment;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;
    @Column(name = "totQty")
    private int totalQuantity;
    @Column(name = "totCost")
    private BigDecimal totalCost;
    // new code--------------------************
    @Column(name = "order_status")
    private String orderStatus;

    public void calculateTotalQuantityAndCost() {
        if (cart != null && cart.getCartItems() != null) {
            totalQuantity = cart.getCartItems().stream().mapToInt(CartItem::getQty).sum();
            totalCost = cart.getCartItems().stream().map(CartItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
// Add fixed delivery charges if the totalCost is below 500 (new code)---------*********
            if (totalCost.compareTo(BigDecimal.valueOf(500)) < 0) {
                totalCost = totalCost.add(FIXED_DELIVERY_CHARGE);
            }
        } else {
            totalQuantity = 0;
            totalCost = BigDecimal.ZERO;
        }
    }
    public void deleteCartItems() {
        if (cart != null && cart.getCartItems() != null) {
            cart.getCartItems().clear();
        }
    }

    public String getAddress() {
        return address;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        if (date == null) {
            throw new IllegalArgumentException("Invalid checkout date. Date should be set before setting the time.");
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isWeekday = dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
        boolean isValidTimeRange = (isWeekday && time.isAfter(LocalTime.of(12, 0)) && time.isBefore(LocalTime.of(16, 0)))
                || (!isWeekday && time.isAfter(LocalTime.of(8, 0)) && time.isBefore(LocalTime.of(11, 0)));
        if (!isWeekday && !dayOfWeek.equals(DayOfWeek.SATURDAY)) {
            throw new IllegalArgumentException("Invalid checkout date. Date should be either a weekday (Mon-Fri) or Saturday.");
        }
        if (!isValidTimeRange) {
            throw new IllegalArgumentException("Invalid checkout time. Time should be between 12pm and 4pm (Mon-Fri) or between 8am and 11am (Sat).");
        }
        this.time = time;
    }

}

