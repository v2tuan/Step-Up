package com.stepup.controller;

import com.stepup.entity.Product;
import com.stepup.entity.Review;
import com.stepup.service.IProductService;
import com.stepup.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private IReviewService service;
    @Autowired
    private IProductService productService;

    @GetMapping("")
    public ResponseEntity<List<Review>> getSearchSuggestions(@RequestParam("id") Long productId) {
        List<Review> reviews = service.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }
    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody Review review) {
        Product product = productService.getProductById(review.getProduct().getId())
                .orElse(null);
        if(product != null) {
            service.saveReview(review);
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID");
    }

}
