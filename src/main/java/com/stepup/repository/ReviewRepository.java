package com.stepup.repository;

import com.stepup.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_Id(Long productId);
    List<Review> findByUser_Id(Long userId);
    void deleteByProduct_Id(Long productId);

    // Tính trung bình đánh giá của sản phẩm
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(Long productId);

    //  Lấy danh sách đánh giá của sản phẩm theo rating
    List<Review> findByProduct_IdAndRating(Long productId, int rating);
}
