package com.ecommerce.service;

import com.ecommerce.dto.CouponDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoService {

    private static final List<CouponDTO> COUPONS = List.of(
            new CouponDTO("WELCOME10", 0.10, 0, "10% off on your first order, no minimum purchase", "31 Dec 2026"),
            new CouponDTO("SAVE20", 0.20, 2000, "20% off on orders above ₹2,000", "31 Dec 2026"),
            new CouponDTO("VEYLO5", 0.05, 0, "Flat 5% off on any order", "31 Dec 2026")
    );

    public List<CouponDTO> getAllCoupons() {
        return COUPONS;
    }

    private CouponDTO findCoupon(String code) {
        if (code == null) return null;
        return COUPONS.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code.trim()))
                .findFirst()
                .orElse(null);
    }

    public double getDiscountPercent(String code, double subtotal) {
        if (code == null || code.isBlank()) {
            return 0.0;
        }
        CouponDTO coupon = findCoupon(code);
        if (coupon == null) {
            throw new RuntimeException("Invalid promo code");
        }
        if (subtotal < coupon.getMinPurchaseAmount()) {
            throw new RuntimeException("Minimum purchase of ₹" + (int) coupon.getMinPurchaseAmount() + " required for this code");
        }
        return coupon.getDiscountPercent();
    }
}