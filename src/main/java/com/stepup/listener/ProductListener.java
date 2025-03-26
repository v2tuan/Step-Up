package com.stepup.listener;

import com.stepup.entity.Product;
import com.stepup.service.redis.ProductRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor // Tự động tạo constructor có tất cả các tham số
public class ProductListener {
    private final ProductRedisService productRedisService;
    private static final Logger logger = LoggerFactory.getLogger(ProductListener.class);

    @PrePersist // Được gọi trước khi đối tượng Product được lưu vào database lần đầu tiên
    public void prePersist(Product product) {
        logger.info("prePersist");
    }

    @PostPersist // Được gọi sau khi đối tượng Product đã được lưu vào database
    public void postPersist(Product product) {
        logger.info("postPersist");
        productRedisService.clear(); // Xóa cache trong Redis để đảm bảo dữ liệu luôn mới
    }

    @PreUpdate // Được gọi trước khi đối tượng Product được cập nhật trong database
    public void preUpdate(Product product) {
        logger.info("preUpdate");
    }

    @PostUpdate // Được gọi sau khi đối tượng Product đã được cập nhật trong database
    public void postUpdate(Product product) {
        logger.info("postUpdate");
        productRedisService.clear(); // Xóa cache để cập nhật dữ liệu mới vào Redis
    }

    @PreRemove // Được gọi trước khi đối tượng Product bị xóa khỏi database
    public void preRemove(Product product) {
        logger.info("preRemove");
    }

    @PostRemove // Được gọi sau khi đối tượng Product đã bị xóa khỏi database
    public void postRemove(Product product) {
        logger.info("postRemove");
        productRedisService.clear(); // Xóa cache để tránh giữ dữ liệu không còn tồn tại
    }
}

