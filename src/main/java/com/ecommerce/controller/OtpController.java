package com.ecommerce.controller;

import com.ecommerce.service.OtpService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class OtpController {

    @Autowired
    private OtpService otpService;

    public static class SendOtpRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        public String email;
    }

    public static class VerifyOtpRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        public String email;

        @NotBlank(message = "OTP code is required")
        public String code;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@jakarta.validation.Valid @RequestBody SendOtpRequest request) {
        otpService.generateAndSendOtp(request.email);
        return ResponseEntity.ok(Map.of("message", "OTP sent to " + request.email));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@jakarta.validation.Valid @RequestBody VerifyOtpRequest request) {
        otpService.verifyOtp(request.email, request.code);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }
}