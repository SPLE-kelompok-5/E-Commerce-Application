package com.app.services;

import java.util.List;

import com.app.payloads.WishlistDTO;

public interface WishlistService {
    
    WishlistDTO addProductToWishlist(Long wishlistId, Long productId);
    
    List<WishlistDTO> getAllWishlists();
    
    WishlistDTO getWishlist(String email, Long wishlistId);
    
    String deleteProductFromWishlist(Long wishlistId, Long productId);
    
    WishlistDTO moveProductToCart(String email, Long wishlistId, Long productId, Integer quantity);
}