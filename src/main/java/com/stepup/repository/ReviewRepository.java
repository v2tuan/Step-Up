package com.stepup.repository;

import com.stepup.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);

    void deleteByProductId(Long productId);

    // Kiểm tra xem người dùng đã đánh giá đơn hàng hay chưa
    boolean existsByOrderIdAndUserId(Long orderId, Long userId);

    // Tính trung bình đánh giá của sản phẩm
    @Query("SELECT COALESCE(AVG(CAST(r.rating AS double)), 0.0) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(Long productId);

    // Lấy danh sách đánh giá của sản phẩm theo rating
    List<Review> findByProductIdAndRating(Long productId, int rating);
}
