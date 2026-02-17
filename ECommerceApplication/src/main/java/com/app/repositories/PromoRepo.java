package com.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entites.Promo;

public interface PromoRepo extends JpaRepository<Promo, Long>  {
	Optional<Promo> findByPromoCodeIgnoreCase(String promoCode);
}
