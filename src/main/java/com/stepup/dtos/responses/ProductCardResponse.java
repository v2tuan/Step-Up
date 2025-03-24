package com.stepup.dtos.responses;

import com.stepup.entity.Color;
import com.stepup.entity.ProductImage;
import com.stepup.entity.ProductVariant;
import com.stepup.entity.VariantGroup;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCardResponse {
    private long id;

    private String name;

    private Double price;

    private String ImageUrl;

    private Double promotionPrice;

    private Double rating;
}
