package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Payment;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.CreatePaymentDTO;
import com.app.payloads.PaymentDTO;
import com.app.repositories.PaymentRepo;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PaymentDTO createPayment(CreatePaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setBankName(paymentDTO.getBankName());
        payment.setStoreAccountNumber(paymentDTO.getStoreAccountNumber());

        Payment saved = paymentRepo.save(payment);
        return modelMapper.map(saved, PaymentDTO.class);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepo.findAll();
        return payments.stream()
                .map(p -> modelMapper.map(p, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getPayment(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "paymentId", paymentId));
        return modelMapper.map(payment, PaymentDTO.class);
    }

    @Override
    public PaymentDTO updatePayment(Long paymentId, CreatePaymentDTO paymentDTO) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "paymentId", paymentId));

        payment.setBankName(paymentDTO.getBankName());
        payment.setStoreAccountNumber(paymentDTO.getStoreAccountNumber());

        Payment saved = paymentRepo.save(payment);
        return modelMapper.map(saved, PaymentDTO.class);
    }

    @Override
    public String deletePayment(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "paymentId", paymentId));

        paymentRepo.delete(payment);
        return "Payment with paymentId " + paymentId + " deleted successfully!!!";
    }
}
