package com.ecommerce.controller;

import com.ecommerce.service.PaymentService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    public static class CreateOrderRequest {
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than 0")
        public Double amount;
    }

    public static class VerifyRequest {
        public String razorpay_order_id;
        public String razorpay_payment_id;
        public String razorpay_signature;
    }

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@jakarta.validation.Valid @RequestBody CreateOrderRequest request) throws Exception {
        return ResponseEntity.ok(paymentService.createOrder(request.amount));
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestBody VerifyRequest request) {
        boolean valid = paymentService.verifyPayment(
                request.razorpay_order_id,
                request.razorpay_payment_id,
                request.razorpay_signature
        );
        if (!valid) {
            throw new RuntimeException("Payment verification failed");
        }
        return ResponseEntity.ok(Map.of("verified", true));
    }
}