package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.CreatePaymentDTO;
import com.app.payloads.PaymentDTO;
import com.app.services.PaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "E-Commerce Application")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody CreatePaymentDTO paymentDTO) {
        PaymentDTO saved = paymentService.createPayment(paymentDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long paymentId) {
        PaymentDTO payment = paymentService.getPayment(paymentId);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @PutMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Long paymentId,
            @Valid @RequestBody CreatePaymentDTO paymentDTO) {
        PaymentDTO updated = paymentService.updatePayment(paymentId, paymentDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable Long paymentId) {
        String status = paymentService.deletePayment(paymentId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
