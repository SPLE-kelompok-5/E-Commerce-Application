package com.app.entites;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "store_discounts")
@AllArgsConstructor
@NoArgsConstructor
public class StoreDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    private String discountName;

    @Min(0)
    @Max(100)
    private double discountPercentage;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
}