package com.example.springboot.service;

import com.example.springboot.entity.Wishlist;
import com.example.springboot.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public List<Wishlist> getWishlistItemsByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    public void deleteWishlistItemsByUserId(Long userId) {
        wishlistRepository.deleteByUserId(userId);
    }


    public Wishlist saveWishlistItem(Wishlist wishlistItem) {
        return wishlistRepository.save(wishlistItem);
    }

    public void deleteWishlistItem(Long id) {
        wishlistRepository.deleteById(id);
    }



}

