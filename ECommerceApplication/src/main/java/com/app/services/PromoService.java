package com.app.services;

import com.app.payloads.CreatePromoDTO;
import com.app.payloads.PromoDTO;
import com.app.payloads.PromoResponse;

public interface PromoService {
    PromoDTO createPromo(CreatePromoDTO promoDTO);
    PromoResponse getAllPromos(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    PromoDTO getPromo(Long promoId);
    PromoDTO updatePromo(Long promoId, CreatePromoDTO promoDTO);
    String deletePromo(Long promoId);
    PromoDTO validatePromoCode(String promoCode);
}
