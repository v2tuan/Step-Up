package com.stepup.controller;

import com.stepup.dtos.requests.ReviewDTO;
import com.stepup.dtos.responses.ReviewResponse;
import com.stepup.entity.Product;
import com.stepup.entity.Review;
import com.stepup.entity.User;
import com.stepup.service.IProductService;
import com.stepup.service.IReviewService;
import com.stepup.service.redis.ReviewRedisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private IReviewService reviewService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ReviewRedisService reviewRedisService;

    @GetMapping("")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@RequestParam("id") Long productId) {
        try {
              // Kiểm tra Redis trước
            List<ReviewResponse> reviews = reviewRedisService.getReviews(productId);
            if (reviews == null) {
                // Cache trống, lấy từ cơ sở dữ liệu
                reviews = reviewService.getReviewsByProductId(productId);
                // Lưu vào Redis
                reviewRedisService.saveReviews(reviews, productId);
            }
            return ResponseEntity.ok(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy đánh giá: " + e.getMessage(), e);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@Valid @ModelAttribute ReviewDTO reviewDTO) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = (User) principal;
            reviewService.createReview(reviewDTO, user);
            return ResponseEntity.ok("Thêm đánh giá thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
