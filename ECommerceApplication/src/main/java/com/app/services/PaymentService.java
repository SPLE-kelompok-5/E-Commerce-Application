package com.app.services;

import java.util.List;

import com.app.payloads.CreatePaymentDTO;
import com.app.payloads.PaymentDTO;

public interface PaymentService {

    PaymentDTO createPayment(CreatePaymentDTO paymentDTO);

    List<PaymentDTO> getAllPayments();

    PaymentDTO getPayment(Long paymentId);

    PaymentDTO updatePayment(Long paymentId, CreatePaymentDTO paymentDTO);

    String deletePayment(Long paymentId);
}
