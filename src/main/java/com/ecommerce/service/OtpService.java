package com.ecommerce.service;

import com.ecommerce.model.Otp;
import com.ecommerce.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Value("${brevo.api-key}")
    private String brevoApiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    private static final int EXPIRY_MINUTES = 5;
    private final RestTemplate restTemplate = new RestTemplate();

    public void generateAndSendOtp(String email) {
        String code = generateCode();

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES));
        otp.setVerified(false);
        otpRepository.save(otp);

        sendEmail(email, code);
    }

    public boolean verifyOtp(String email, String code) {
        Otp otp = otpRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("No OTP found for this email. Please request one first."));

        if (otp.isVerified()) {
            throw new RuntimeException("This OTP has already been used.");
        }
        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }
        if (!otp.getCode().equals(code)) {
            throw new RuntimeException("Incorrect OTP. Please try again.");
        }

        otp.setVerified(true);
        otpRepository.save(otp);
        return true;
    }

    public boolean isEmailVerified(String email) {
        return otpRepository.findTopByEmailOrderByIdDesc(email)
                .map(otp -> otp.isVerified() && LocalDateTime.now().isBefore(otp.getExpiresAt().plusMinutes(10)))
                .orElse(false);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendEmail(String toEmail, String code) {
        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", brevoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> sender = new HashMap<>();
        sender.put("name", "Veylo");
        sender.put("email", senderEmail);

        Map<String, String> recipient = new HashMap<>();
        recipient.put("email", toEmail);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", sender);
        body.put("to", List.of(recipient));
        body.put("subject", "Your Veylo verification code");
        body.put("htmlContent",
                "<p>Your OTP is: <strong>" + code + "</strong></p>" +
                "<p>This code expires in " + EXPIRY_MINUTES + " minutes.</p>" +
                "<p>If you didn't request this, you can safely ignore this email.</p>");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, String.class);
    }
}