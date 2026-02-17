package com.app.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentDTO {

    @NotBlank(message = "Bank name must not be empty")
    private String bankName;

    @NotBlank(message = "Store account number must not be empty")
    private String storeAccountNumber;
}
