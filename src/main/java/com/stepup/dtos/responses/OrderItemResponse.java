package com.stepup.dtos.responses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stepup.entity.Order;
import com.stepup.entity.ProductVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private long id;
    private String title;
    private ProductVariant productVariant;
    private Double price;
    private Double promotionPrice;
    private int count;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
