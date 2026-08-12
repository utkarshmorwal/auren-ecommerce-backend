package com.ecommerce.controller;

import com.ecommerce.dto.CouponDTO;
import com.ecommerce.service.PromoService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/promo")
public class PromoController {

    @Autowired
    private PromoService promoService;

    public static class PromoRequest {
        @NotBlank(message = "Promo code is required")
        public String code;

        @NotNull(message = "Subtotal is required")
        @PositiveOrZero(message = "Subtotal cannot be negative")
        public Double subtotal;
    }

    @GetMapping("/list")
    public ResponseEntity<List<CouponDTO>> listCoupons() {
        return ResponseEntity.ok(promoService.getAllCoupons());
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@jakarta.validation.Valid @RequestBody PromoRequest request) {
        double discount = promoService.getDiscountPercent(request.code, request.subtotal);
        return ResponseEntity.ok(Map.of(
                "valid", true,
                "discountPercent", discount,
                "message", "Promo code applied: " + Math.round(discount * 100) + "% off"
        ));
    }
}