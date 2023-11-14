package com.example.springboot.repository;

import com.example.springboot.entity.Cart;
import com.example.springboot.entity.Checkout;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CheckoutRepository  extends JpaRepository<Checkout, Long> {
    List<Checkout> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart = :cart")
    void deleteCartItemsByCart(@Param("cart") Cart cart);


}
