package com.app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.config.AppConstants;
import com.app.entites.Promo;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CreatePromoDTO;
import com.app.payloads.PromoDTO;
import com.app.payloads.PromoResponse;
import com.app.repositories.PromoRepo;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoRepo promoRepo;

    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public PromoDTO createPromo(CreatePromoDTO promoDTO) {

        String code = promoDTO.getPromoCode();
        if (code == null || code.trim().isEmpty()) {
            throw new APIException("Promo code must not be empty");
        }

        boolean exists = promoRepo.findAll().stream()
                .anyMatch(p -> p.getPromoCode().equalsIgnoreCase(code.trim()));
        if (exists) {
            throw new APIException("Promo code already exists: " + code);
        }

        double discount = promoDTO.getDiscount();
        if (discount <= 0 || discount > 100) {
            throw new APIException("Promo discount must be between 0 and 100 percent");
        }

        LocalDate today = LocalDate.now();
        LocalDate expiry = promoDTO.getExpiry();
        if (expiry == null || !expiry.isAfter(today)) {
            throw new APIException("Expiry date must be after today");
        }

        Promo promo = new Promo();
        promo.setPromoCode(code.trim());
        promo.setDiscount(discount);
        promo.setExpiry(expiry);

        Promo saved = promoRepo.save(promo);

        return modelMapper.map(saved, PromoDTO.class);
    }

    @Override
    public PromoDTO validatePromoCode(String promoCode) {
        if (promoCode == null || promoCode.trim().isEmpty()) {
            throw new APIException("Promo code must not be empty");
        }

        Promo promo = promoRepo.findByPromoCodeIgnoreCase(promoCode.trim())
                .orElseThrow(() -> new APIException("Invalid promo code: " + promoCode));

        LocalDate today = LocalDate.now();
        if (promo.getExpiry() == null || !promo.getExpiry().isAfter(today)) {
            throw new APIException("Promo code has expired: " + promoCode);
        }

        return modelMapper.map(promo, PromoDTO.class);
    }

    @Override
    public PromoResponse getAllPromos(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Promo> pagePromos = promoRepo.findAll(pageDetails);
        List<Promo> promos = pagePromos.getContent();

        List<PromoDTO> promoDTOs = promos.stream()
                .map(promo -> modelMapper.map(promo, PromoDTO.class))
                .collect(Collectors.toList());

        PromoResponse promoResponse = new PromoResponse();
        promoResponse.setContent(promoDTOs);
        promoResponse.setPageNumber(pagePromos.getNumber());
        promoResponse.setPageSize(pagePromos.getSize());
        promoResponse.setTotalElements(pagePromos.getTotalElements());
        promoResponse.setTotalPages(pagePromos.getTotalPages());
        promoResponse.setLastPage(pagePromos.isLast());

        return promoResponse;
    }

    @Override
    public PromoDTO getPromo(Long promoId) {
        Promo promo = promoRepo.findById(promoId)
                .orElseThrow(() -> new ResourceNotFoundException("Promo", "promoId", promoId));

        return modelMapper.map(promo, PromoDTO.class);
    }

    @Override
    public PromoDTO updatePromo(Long promoId, CreatePromoDTO promoDTO) {
        Promo promo = promoRepo.findById(promoId)
                .orElseThrow(() -> new ResourceNotFoundException("Promo", "promoId", promoId));

        String code = promoDTO.getPromoCode();
        if (code == null || code.trim().isEmpty()) {
            throw new APIException("Promo code must not be empty");
        }

        String trimmedCode = code.trim();
        boolean existsWithOtherId = promoRepo.findAll().stream()
                .anyMatch(p -> p.getPromoId() != null
                        && !p.getPromoId().equals(promoId)
                        && p.getPromoCode().equalsIgnoreCase(trimmedCode));
        if (existsWithOtherId) {
            throw new APIException("Promo code already exists: " + trimmedCode);
        }

        double discount = promoDTO.getDiscount();
        if (discount <= 0 || discount > 100) {
            throw new APIException("Promo discount must be between 0 and 100 percent");
        }

        LocalDate today = LocalDate.now();
        LocalDate expiry = promoDTO.getExpiry();
        if (expiry == null || !expiry.isAfter(today)) {
            throw new APIException("Expiry date must be after today");
        }

        promo.setPromoCode(trimmedCode);
        promo.setDiscount(discount);
        promo.setExpiry(expiry);

        Promo saved = promoRepo.save(promo);

        return modelMapper.map(saved, PromoDTO.class);
    }

    @Override
    public String deletePromo(Long promoId) {
        Promo promo = promoRepo.findById(promoId)
                .orElseThrow(() -> new ResourceNotFoundException("Promo", "promoId", promoId));

        promoRepo.delete(promo);

        return "Promo with promoId " + promoId + " deleted successfully!!!";
    }
}
