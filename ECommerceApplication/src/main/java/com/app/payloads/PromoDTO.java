package com.app.payloads;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoDTO {
    private Long promoId;
    private String promoCode;
    private double discount;
    private LocalDate expiry;
}
