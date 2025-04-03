package com.stepup.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stepup.entity.Cart;
import com.stepup.entity.ProductVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemRespone {
    private long id;

    private String title;

    private ProductVariant productVariant;

    private int count;
}
