package com.ecommerce.controller;

import com.ecommerce.dto.ReviewDTO;
import com.ecommerce.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/product/{productId}")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewDTO dto,
            Authentication authentication) {
        ReviewDTO created = reviewService.addReview(productId, authentication.getName(), dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsForProduct(productId));
    }
}