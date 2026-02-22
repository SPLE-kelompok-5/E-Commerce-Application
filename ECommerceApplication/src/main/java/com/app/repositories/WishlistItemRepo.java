package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.WishlistItem;

@Repository
public interface WishlistItemRepo extends JpaRepository<WishlistItem, Long> {

    @Query("SELECT wi FROM WishlistItem wi WHERE wi.wishlist.wishlistId = ?1 AND wi.product.productId = ?2")
    WishlistItem findWishlistItemByProductIdAndWishlistId(Long wishlistId, Long productId);

    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.wishlist.wishlistId = ?1 AND wi.product.productId = ?2")
    void deleteWishlistItemByProductIdAndWishlistId(Long wishlistId, Long productId);
}