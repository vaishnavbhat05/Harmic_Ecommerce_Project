package com.example.springboot.repository;

import com.example.springboot.entity.CartItem;
import com.example.springboot.entity.Products;
import com.example.springboot.entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Transactional
    @Query("update CartItem c set c.total=:total where c.cart=:cartId and c.products=:prodId")
    public void insertTot(@Param("total") BigDecimal total, @Param("prodId") Products prodId, @Param("cartId") Cart cartId);

    List<CartItem> findByCartId(Long cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.products = :products and ci.cart = :cart")
    CartItem findByProductsId(@Param("products")Products products, @Param("cart")Cart cart);

    @Query("SELECT COALESCE(SUM(ci.qty), 0) FROM CartItem ci WHERE ci.cart = :cart")
    int calculateTotalQuantityByCart(@Param("cart") Cart cart);

    @Query("SELECT SUM(ci.total) FROM CartItem ci WHERE ci.cart = :cart")
    BigDecimal calculateTotalCostByCart(Cart cart);
}
