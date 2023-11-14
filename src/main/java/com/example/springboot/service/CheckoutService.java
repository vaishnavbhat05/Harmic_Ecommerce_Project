package com.example.springboot.service;

import com.example.springboot.entity.Cart;
import com.example.springboot.entity.Checkout;
import com.example.springboot.repository.CartItemRepository;
import com.example.springboot.repository.CheckoutRepository;
import com.example.springboot.DTO.CheckoutResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CheckoutService {
    @Autowired
    private CheckoutRepository checkoutRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<CheckoutResponseDTO> getCheckoutHistoryByUserId(Long userId) {
        List<Checkout> checkoutHistory = checkoutRepository.findByUserId(userId);
        return mapToCheckoutResponseDTOList(checkoutHistory);
    }
    private List<CheckoutResponseDTO> mapToCheckoutResponseDTOList(List<Checkout> checkoutList) {
        List<CheckoutResponseDTO> responseDTOList = new ArrayList<>();
        for (Checkout checkout : checkoutList) {
            String firstName = checkout.getUser().getFirstName();
            String lastName = checkout.getUser().getLastName();
            String phone = checkout.getUser().getPhone();
            int qty = checkout.getTotalQuantity();
            BigDecimal total = checkout.getTotalCost();
            String address = checkout.getAddress();
            String payment = checkout.getPayment();
            LocalDate date = checkout.getDate();
            LocalTime time = checkout.getTime();
            CheckoutResponseDTO responseDTO = new CheckoutResponseDTO(firstName, lastName, phone, qty, total,
                    address, payment, date, time);
            responseDTOList.add(responseDTO);
        }
        return responseDTOList;
    }
    @Transactional
    public Checkout addCheckout(Checkout checkout) {
        Cart cart = checkout.getCart();
        if (cart != null) {
// checkoutRepository.clearCartItemsByCart(cart);
            int totalQuantity = cartItemRepository.calculateTotalQuantityByCart(cart);
            BigDecimal totalCost = cartItemRepository.calculateTotalCostByCart(cart);
// Add fixed delivery charges if the totalCost is below 500 new code ----------*-***************
            if (totalCost.compareTo(BigDecimal.valueOf(500)) < 0) {
                totalCost = totalCost.add(Checkout.FIXED_DELIVERY_CHARGE);
            }
            checkout.setTotalQuantity(totalQuantity);
            checkout.setTotalCost(totalCost);
            checkoutRepository.deleteCartItemsByCart(cart);
// Set the initial order status (e.g., "Dispatched") when a new checkout is added new code -**********
            checkout.setOrderStatus("Order Placed");
        }
        return checkoutRepository.save(checkout);
    }
    public List<Checkout> getAllCheckouts() {
        return checkoutRepository.findAll();
    }
    public Checkout getCheckoutById(Long checkoutId) {
        Optional<Checkout> optionalCheckout = checkoutRepository.findById(checkoutId);
        return optionalCheckout.orElse(null);
    }

    public Checkout updateCheckout(Checkout checkout) {
        return checkoutRepository.save(checkout);
    }
    public void deleteCheckout(Long checkoutId) {
        checkoutRepository.deleteById(checkoutId);
    }

// new code for order status ---------------------****************

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Checkout updateOrderStatus(Long checkoutId, String orderStatus) {
        Checkout checkout = checkoutRepository.findById(checkoutId).orElse(null);
        if (checkout != null) {
            checkout.setOrderStatus(orderStatus);
            return checkoutRepository.save(checkout);
        }
        return null;
    }
}
