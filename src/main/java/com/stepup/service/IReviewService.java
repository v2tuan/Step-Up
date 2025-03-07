package com.stepup.service;
import com.stepup.entity.Review;

import java.util.List;
import java.util.Optional;
public interface IReviewService {

    // Lấy danh sách đánh giá theo sản phẩm
    List<Review> getReviewsByProductId(Long productId);

    //  Lấy danh sách đánh giá theo người dùng
    List<Review> getReviewsByUserId(Long userId);

    // Lấy đánh giá theo ID
    Optional<Review> getReviewById(Long reviewId);

    //  Tính trung bình đánh giá của sản phẩm
    Double getAverageRatingByProductId(Long productId);

    //  Thêm hoặc cập nhật đánh giá
    Review saveReview(Review review);

    //  Lấy danh sách đánh giá của sản phẩm theo rating
    List<Review> getReviewsByProductIdAndRating(Long productId, int rating);

    //  Xóa đánh giá theo ID
    void deleteReview(Long reviewId);

    // Xóa tất cả đánh giá của một sản phẩm
    void deleteReviewsByProductId(Long productId);
}
