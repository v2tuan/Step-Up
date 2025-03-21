package com.stepup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "is_active")
    private Boolean isActive;

    @CreationTimestamp // Tự động gán thời gian tạo bản ghi.
    private LocalDateTime createdAt;

    @UpdateTimestamp // Tự động cập nhật thời gian khi bản ghi bị sửa đổi.
    private LocalDateTime updatedAt;
}
