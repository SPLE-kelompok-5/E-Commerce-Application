package com.app.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.StoreDiscount;

@Repository
public interface StoreDiscountRepo extends JpaRepository<StoreDiscount, Long> {
    // Applied discount is the discount which start date <= current date <= end date
    @Query("SELECT s FROM StoreDiscount s WHERE s.active = true AND s.startDate <= ?1 AND s.endDate >= ?1")
    Optional<StoreDiscount> findActiveDiscount(LocalDate currentDate);
}