package com.stepup.dtos.responses;

import com.stepup.entity.ProductImage;
import com.stepup.entity.ProductVariant;
import com.stepup.entity.VariantGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String name;
    private String slug;
    private String description;
    private boolean isActive;

    private Double price;
    private Double promotionPrice;

    List<ProductImage> productImages;

    private String thumbnail;
}
