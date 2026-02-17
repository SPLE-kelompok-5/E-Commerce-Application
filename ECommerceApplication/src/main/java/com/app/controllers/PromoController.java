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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.AppConstants;
import com.app.payloads.CreatePromoDTO;
import com.app.payloads.PromoDTO;
import com.app.payloads.PromoResponse;
import com.app.services.PromoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class PromoController {
	
	@Autowired
	public PromoService promoService;
	
	@PostMapping("/admin/promo")
    public ResponseEntity<PromoDTO> createPromo(@Valid @RequestBody CreatePromoDTO createPromoDTO) {
        PromoDTO promo = promoService.createPromo(createPromoDTO);
		
		return new ResponseEntity<PromoDTO>(promo, HttpStatus.CREATED);
	}

	@GetMapping("/admin/promos")
	public ResponseEntity<PromoResponse> getAllPromos(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
		
		PromoResponse promoResponse = promoService.getAllPromos(pageNumber, pageSize, sortBy, sortOrder);

		return new ResponseEntity<PromoResponse>(promoResponse, HttpStatus.FOUND);
	}
	
    @GetMapping("/public/promos/{promoId}")
    public ResponseEntity<PromoDTO> getPromo(@PathVariable Long promoId) {
        PromoDTO promo = promoService.getPromo(promoId);
        
        return new ResponseEntity<PromoDTO>(promo, HttpStatus.FOUND);
    }

    @PutMapping("/admin/promos/{promoId}")
    public ResponseEntity<PromoDTO> updatePromo(@PathVariable Long promoId, @Valid @RequestBody CreatePromoDTO createPromoDTO) {
        PromoDTO updatedPromo = promoService.updatePromo(promoId, createPromoDTO);
        
        return new ResponseEntity<PromoDTO>(updatedPromo, HttpStatus.OK);
    }

    @DeleteMapping("/admin/promos/{promoId}")
    public ResponseEntity<String> deletePromo(@PathVariable Long promoId) {
        String status = promoService.deletePromo(promoId);
        
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
