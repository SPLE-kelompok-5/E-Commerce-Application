package com.app.entites;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long promoId;

	@NotBlank
	@Size(min = 3, message = "Promo code must contain atleast 3 characters")
	private String promoCode;
	
	@Positive(message = "Discount must be greater than 0")
	private double discount;

	private LocalDate expiry;

}
