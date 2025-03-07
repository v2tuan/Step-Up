package com.stepup.service.impl;

import com.stepup.entity.Review;
import com.stepup.repository.ReviewRepository;
import com.stepup.service.IReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements IReviewService {
    private ReviewRepository repo;
    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        return repo.findByProduct_Id(productId);
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) {
        return repo.findByUser_Id(userId);
    }

    @Override
    public Optional<Review> getReviewById(Long reviewId) {
        return repo.findById(reviewId);
    }

    @Override
    public Double getAverageRatingByProductId(Long productId) {
        return repo.getAverageRatingByProductId(productId);
    }

    @Override
    public Review saveReview(Review review) {
        return repo.save(review);
    }

    @Override
    public List<Review> getReviewsByProductIdAndRating(Long productId, int rating) {
        return repo.findByProduct_IdAndRating(productId, rating);
    }

    @Override
    public void deleteReview(Long reviewId) {
            repo.deleteById(reviewId);
    }

    @Override
    public void deleteReviewsByProductId(Long productId) {
            repo.deleteByProduct_Id(productId);
    }
}
