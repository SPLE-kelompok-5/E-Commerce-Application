package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.Wishlist;
import com.app.entites.WishlistItem;
import com.app.entites.User;
import com.app.entites.Cart;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.WishlistDTO;
import com.app.payloads.ProductDTO;
import com.app.repositories.ProductRepo;
import com.app.repositories.WishlistItemRepo;
import com.app.repositories.WishlistRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private WishlistItemRepo wishlistItemRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public WishlistDTO addProductToWishlist(Long wishlistId, Long productId) {
        
        Wishlist wishlist = wishlistRepo.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        WishlistItem wishlistItem = wishlistItemRepo.findWishlistItemByProductIdAndWishlistId(wishlistId, productId);

        if (wishlistItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the wishlist");
        }

        WishlistItem newWishlistItem = new WishlistItem();
        newWishlistItem.setProduct(product);
        newWishlistItem.setWishlist(wishlist);

        wishlistItemRepo.save(newWishlistItem);

        WishlistDTO wishlistDTO = modelMapper.map(wishlist, WishlistDTO.class);

        List<ProductDTO> productDTOs = wishlist.getWishlistItems().stream()
                .map(wi -> modelMapper.map(wi.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());

        wishlistDTO.setProducts(productDTOs);

        return wishlistDTO;
    }

    @Override
    public List<WishlistDTO> getAllWishlists() {
        
        List<Wishlist> wishlists = wishlistRepo.findAll();

        if (wishlists.size() == 0) {
            throw new APIException("No wishlist exists");
        }

        List<WishlistDTO> wishlistDTOs = wishlists.stream().map(wishlist -> {
            WishlistDTO wishlistDTO = modelMapper.map(wishlist, WishlistDTO.class);

            List<ProductDTO> products = wishlist.getWishlistItems().stream()
                    .map(wi -> modelMapper.map(wi.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());

            wishlistDTO.setProducts(products);

            return wishlistDTO;

        }).collect(Collectors.toList());

        return wishlistDTOs;
    }

    @Override
    public WishlistDTO getWishlist(String email, Long wishlistId) {
        
        Wishlist wishlist = wishlistRepo.findWishlistByEmailAndWishlistId(email, wishlistId);

        if (wishlist == null) {
            throw new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId);
        }

        WishlistDTO wishlistDTO = modelMapper.map(wishlist, WishlistDTO.class);

        List<ProductDTO> products = wishlist.getWishlistItems().stream()
                .map(wi -> modelMapper.map(wi.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());

        wishlistDTO.setProducts(products);

        return wishlistDTO;
    }

    @Override
    public String deleteProductFromWishlist(Long wishlistId, Long productId) {
        
        Wishlist wishlist = wishlistRepo.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId));

        WishlistItem wishlistItem = wishlistItemRepo.findWishlistItemByProductIdAndWishlistId(wishlistId, productId);

        if (wishlistItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        wishlistItemRepo.deleteWishlistItemByProductIdAndWishlistId(wishlistId, productId);

        return "Product " + wishlistItem.getProduct().getProductName() + " removed from the wishlist!";
    }

    @Override
    public WishlistDTO moveProductToCart(String email, Long wishlistId, Long productId, Integer quantity) {
        
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Cart cart = user.getCart();

        if (cart == null) {
            throw new APIException("Cart not found for user: " + email);
        }

        Wishlist wishlist = wishlistRepo.findWishlistByEmailAndWishlistId(email, wishlistId);

        if (wishlist == null) {
            throw new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId);
        }

        WishlistItem wishlistItem = wishlistItemRepo.findWishlistItemByProductIdAndWishlistId(wishlistId, productId);

        if (wishlistItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        // Add product to cart
        cartService.addProductToCart(cart.getCartId(), productId, quantity);

        // Remove from wishlist
        wishlistItemRepo.deleteWishlistItemByProductIdAndWishlistId(wishlistId, productId);

        WishlistDTO wishlistDTO = modelMapper.map(wishlist, WishlistDTO.class);

        List<ProductDTO> products = wishlist.getWishlistItems().stream()
                .map(wi -> modelMapper.map(wi.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());

        wishlistDTO.setProducts(products);

        return wishlistDTO;
    }
}