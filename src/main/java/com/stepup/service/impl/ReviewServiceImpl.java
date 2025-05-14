package com.stepup.service.impl;

import com.stepup.dtos.requests.ReviewDTO;
import com.stepup.dtos.responses.ReviewResponse;
import com.stepup.entity.*;
import com.stepup.mapper.IReviewMapper;
import com.stepup.repository.OrderRepository;
import com.stepup.repository.ProductRepository;
import com.stepup.repository.ReviewRepository;
import com.stepup.service.CloudinaryService;
import com.stepup.service.IReviewService;
import com.stepup.service.redis.ReviewRedisService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewRepository repo;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private IReviewMapper reviewMapper;
    @Autowired
    private ReviewRedisService reviewRedisService;

    @Override
    @Transactional
    public void createReview(ReviewDTO reviewDTO, User user) {
        // Kiểm tra sự tồn tại của sản phẩm
        Product product = productRepository.findProductByProductVariantId(reviewDTO.getProductVariantId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm " ));

        // Kiểm tra sự tồn tại của đơn hàng
        Order order = orderRepository.findById(reviewDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng" ));
        // Tạo đánh giá mới
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setOrder(order);
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());

        // Tải ảnh lên Cloudinary và tạo các thực thể ReviewImage
        if (reviewDTO.getImages() != null && !reviewDTO.getImages().isEmpty()) {
            MultipartFile[] files = reviewDTO.getImages().toArray(new MultipartFile[0]);
            List<Map<String, String>> uploadResults = cloudinaryService.uploadMultipleFiles(files);

            List<ReviewImage> reviewImages = uploadResults.stream()
                    .filter(result -> !result.containsKey("error"))
                    .map(result -> {
                        ReviewImage reviewImage = new ReviewImage();
                        reviewImage.setImageUrl(result.get("url"));
                        reviewImage.setReview(review);
                        return reviewImage;
                    })
                    .collect(Collectors.toList());

            if (reviewImages.isEmpty() && !uploadResults.isEmpty()) {
                throw new RuntimeException("Không thể tải lên tất cả ảnh. Vui lòng thử lại.");
            }

            review.setImages(reviewImages);
        }
        // Lưu đánh giá
//        Product product1 = productRepository.findById(product.getId()) .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm " ));
        repo.save(review);
        reviewRedisService.invalidateCache(product.getId());
        Double averageRating = repo.getAverageRatingByProductId(product.getId());
        product.setRating(averageRating);
        productRepository.save(product);
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        // Kiểm tra sự tồn tại của sản phẩm
        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm " ));

        // Lấy danh sách đánh giá
        List<Review> reviews = repo.findByProductId(productId);
        return reviews.stream()
                .map(reviewMapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) {
        return repo.findByUserId(userId);
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
        return repo.findByProductIdAndRating(productId, rating);
    }

    @Override
    public void deleteReview(Long reviewId) {
            repo.deleteById(reviewId);
    }

    @Override
    public void deleteReviewsByProductId(Long productId) {
            repo.deleteByProductId(productId);
    }
}
