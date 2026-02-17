package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.StoreDiscountDTO;
import com.app.services.StoreDiscountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class StoreDiscountController {

    @Autowired
    private StoreDiscountService storeDiscountService;

    @PostMapping("/admin/store-discounts")
    public ResponseEntity<StoreDiscountDTO> createStoreDiscount(@RequestBody StoreDiscountDTO discountDTO) {
        StoreDiscountDTO createdDiscount = storeDiscountService.createStoreDiscount(discountDTO);
        return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/public/store-discounts/active")
    public ResponseEntity<StoreDiscountDTO> getActiveDiscount() {
        StoreDiscountDTO discount = storeDiscountService.getActiveDiscount();
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @GetMapping("/admin/store-discounts")
    public ResponseEntity<List<StoreDiscountDTO>> getAllDiscounts() {
        List<StoreDiscountDTO> discounts = storeDiscountService.getAllDiscounts();
        return new ResponseEntity<>(discounts, HttpStatus.OK);
    }

    @PutMapping("/admin/store-discounts/{discountId}")
    public ResponseEntity<StoreDiscountDTO> updateDiscount(
            @PathVariable Long discountId, 
            @RequestBody StoreDiscountDTO discountDTO) {
        StoreDiscountDTO updatedDiscount = storeDiscountService.updateDiscount(discountId, discountDTO);
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @DeleteMapping("/admin/store-discounts/{discountId}")
    public ResponseEntity<String> deleteDiscount(@PathVariable Long discountId) {
        storeDiscountService.deleteDiscount(discountId);
        return new ResponseEntity<>("Store discount deleted successfully", HttpStatus.OK);
    }
}