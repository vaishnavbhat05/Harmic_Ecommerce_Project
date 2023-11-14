package com.example.springboot.service;

import com.example.springboot.entity.Cart;
import com.example.springboot.entity.CartItem;
import com.example.springboot.entity.Products;
import com.example.springboot.entity.Stock;
import com.example.springboot.repository.CartItemRepository;
import com.example.springboot.repository.StockRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class CartItemServiceImpl implements CartItemService{
    private CartItemRepository cartitemRepository;
    private StockRepository stockRepository;

    public CartItemServiceImpl(CartItemRepository cartitemRepository, StockRepository stockRepository) {
        this.cartitemRepository = cartitemRepository;
        this.stockRepository=stockRepository;
    }

    @Override
    public List<CartItem> getAllCartItem() {
        return cartitemRepository.findAll();
    }

    @Override
    public CartItem addItem(CartItem newCartItem) {
        return cartitemRepository.save(newCartItem);
    }

    @Override
    public CartItem findById(Long cartitem_id) {
        return cartitemRepository.findById(cartitem_id).orElse(null);
    }

    @Override
    public void deleteCartItemById(Long cartitem_id) {
        CartItem item = cartitemRepository.findById(cartitem_id).orElse(null);
        if(item!=null) {
            Integer qty = item.getQty();
            Products pid=item.getProducts();
            Stock stock = stockRepository.findByProducts(pid);
            stock.setQty(stock.getQty()+qty);
            stockRepository.save(stock);
            cartitemRepository.deleteById(cartitem_id);
        }
    }

    @Override
    public void deleteAll() {
        cartitemRepository.deleteAll();
    }

    @Override
    public CartItem findByProduct(Products products, Cart cart){
        return cartitemRepository.findByProductsId(products,cart);
    }

    @Override
    public BigDecimal calcTotal(BigDecimal price, int qty) {
        return price.multiply(BigDecimal.valueOf(qty));
    }

    @Override
    public String updateQty(Long id, CartItem updatedQty) {
        Optional<CartItem> Item = cartitemRepository.findById(id);
        if (Item.isPresent()) {
            Integer oldQty=Item.get().getQty();
            CartItem existingItem = Item.get();
            if (updatedQty.getQty() != null) {
                existingItem.setQty(updatedQty.getQty());
                existingItem.setTotal(existingItem.getProducts().getPrice().multiply(BigDecimal.valueOf(updatedQty.getQty())));
                Integer stock = stockRepository.checkAvailability(existingItem.getProducts());
                Stock prodStock = stockRepository.findByProducts(existingItem.getProducts());
                if (oldQty > updatedQty.getQty()) {
                    stock = stock + (oldQty - updatedQty.getQty());
                } else {
                    stock = stock - (updatedQty.getQty() - oldQty);
                }
                if (stock >= 0) {
                    prodStock.setQty(stock);
                    stockRepository.save(prodStock);
                    cartitemRepository.save(existingItem);
                    return "Quantity updated successfully.";
                }
                return "Could not update quantity!! Only " + prodStock.getQty() + " available in stock.";
            }
        }
        return "Product not found in cart.";
    }

    @Override
    public List<CartItem> getCartitemByCartId(Long cartId) {
        return cartitemRepository.findByCartId(cartId);
    }

    @Override
    public CartItem updateItem(Long cartItemId,int qty){
        CartItem item=cartitemRepository.findById(cartItemId).orElse(null);
        item.setQty(qty);
        return cartitemRepository.save(item);
    }
}
