package com.app.payloads;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePromoDTO {
    private String promoCode;
    private double discount;
    private LocalDate expiry;
}
