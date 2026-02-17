package com.app.services;

import java.util.List;

import com.app.payloads.StoreDiscountDTO;

public interface StoreDiscountService {
    
    StoreDiscountDTO createStoreDiscount(StoreDiscountDTO discountDTO);
    
    StoreDiscountDTO getActiveDiscount();
    
    List<StoreDiscountDTO> getAllDiscounts();
    
    StoreDiscountDTO updateDiscount(Long discountId, StoreDiscountDTO discountDTO);
    
    void deleteDiscount(Long discountId);
}