package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Wishlist;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Long> {

    @Query("SELECT w FROM Wishlist w JOIN w.user u WHERE u.email = ?1 AND w.wishlistId = ?2")
    Wishlist findWishlistByEmailAndWishlistId(String email, Long wishlistId);
}