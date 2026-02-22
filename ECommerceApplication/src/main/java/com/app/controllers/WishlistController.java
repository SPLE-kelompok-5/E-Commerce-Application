package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.WishlistDTO;
import com.app.services.WishlistService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/public/wishlists/{wishlistId}/products/{productId}")
    public ResponseEntity<WishlistDTO> addProductToWishlist(@PathVariable Long wishlistId,
            @PathVariable Long productId) {

        WishlistDTO wishlistDTO = wishlistService.addProductToWishlist(wishlistId, productId);

        return new ResponseEntity<WishlistDTO>(wishlistDTO, HttpStatus.CREATED);
    }

    @GetMapping("/admin/wishlists")
    public ResponseEntity<List<WishlistDTO>> getAllWishlists() {

        List<WishlistDTO> wishlistDTOs = wishlistService.getAllWishlists();

        return new ResponseEntity<List<WishlistDTO>>(wishlistDTOs, HttpStatus.OK);
    }

    @GetMapping("/public/users/{email}/wishlists/{wishlistId}")
    public ResponseEntity<WishlistDTO> getWishlistById(@PathVariable String email,
            @PathVariable Long wishlistId) {

        WishlistDTO wishlistDTO = wishlistService.getWishlist(email, wishlistId);

        return new ResponseEntity<WishlistDTO>(wishlistDTO, HttpStatus.OK);
    }

    @DeleteMapping("/public/wishlists/{wishlistId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromWishlist(@PathVariable Long wishlistId,
            @PathVariable Long productId) {

        String status = wishlistService.deleteProductFromWishlist(wishlistId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

    @PostMapping("/public/users/{email}/wishlists/{wishlistId}/products/{productId}/move-to-cart")
    public ResponseEntity<WishlistDTO> moveProductToCart(@PathVariable String email,
            @PathVariable Long wishlistId,
            @PathVariable Long productId,
            @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {

        WishlistDTO wishlistDTO = wishlistService.moveProductToCart(email, wishlistId, productId, quantity);

        return new ResponseEntity<WishlistDTO>(wishlistDTO, HttpStatus.OK);
    }
}