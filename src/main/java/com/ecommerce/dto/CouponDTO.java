package com.ecommerce.dto;

public class CouponDTO {
    private String code;
    private double discountPercent;
    private double minPurchaseAmount;
    private String description;
    private String expiresAt;

    public CouponDTO() {}

    public CouponDTO(String code, double discountPercent, double minPurchaseAmount, String description, String expiresAt) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.minPurchaseAmount = minPurchaseAmount;
        this.description = description;
        this.expiresAt = expiresAt;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }
    public double getMinPurchaseAmount() { return minPurchaseAmount; }
    public void setMinPurchaseAmount(double minPurchaseAmount) { this.minPurchaseAmount = minPurchaseAmount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getExpiresAt() { return expiresAt; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
}