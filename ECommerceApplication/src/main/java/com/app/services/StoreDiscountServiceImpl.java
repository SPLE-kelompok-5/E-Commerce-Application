package com.app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.StoreDiscount;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.StoreDiscountDTO;
import com.app.repositories.StoreDiscountRepo;

@Service
public class StoreDiscountServiceImpl implements StoreDiscountService {

    @Autowired
    private StoreDiscountRepo storeDiscountRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StoreDiscountDTO createStoreDiscount(StoreDiscountDTO discountDTO) {
        StoreDiscount discount = modelMapper.map(discountDTO, StoreDiscount.class);
        StoreDiscount savedDiscount = storeDiscountRepo.save(discount);
        return modelMapper.map(savedDiscount, StoreDiscountDTO.class);
    }

    @Override
    public StoreDiscountDTO getActiveDiscount() {
        StoreDiscount discount = storeDiscountRepo.findActiveDiscount(LocalDate.now())
                .orElse(null);
        
        if (discount == null) {
            return null;
        }
        
        return modelMapper.map(discount, StoreDiscountDTO.class);
    }

    @Override
    public List<StoreDiscountDTO> getAllDiscounts() {
        List<StoreDiscount> discounts = storeDiscountRepo.findAll();
        return discounts.stream()
                .map(discount -> modelMapper.map(discount, StoreDiscountDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public StoreDiscountDTO updateDiscount(Long discountId, StoreDiscountDTO discountDTO) {
        StoreDiscount discount = storeDiscountRepo.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Store Discount", "discountId", discountId));
        
        discount.setDiscountName(discountDTO.getDiscountName());
        discount.setDiscountPercentage(discountDTO.getDiscountPercentage());
        discount.setStartDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setActive(discountDTO.isActive());
        
        StoreDiscount updatedDiscount = storeDiscountRepo.save(discount);
        return modelMapper.map(updatedDiscount, StoreDiscountDTO.class);
    }

    @Override
    public void deleteDiscount(Long discountId) {
        StoreDiscount discount = storeDiscountRepo.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Store Discount", "discountId", discountId));
        
        storeDiscountRepo.delete(discount);
    }
}