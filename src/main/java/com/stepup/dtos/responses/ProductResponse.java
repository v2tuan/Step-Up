package com.stepup.dtos.responses;

import com.stepup.entity.ProductImage;
import com.stepup.entity.ProductVariant;
import com.stepup.entity.VariantGroup;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

public class ProductResponse {
    private long id;
    private String name;
    private String slug;
    private String description;
    private boolean isActive;

    @OneToMany(mappedBy = "product")
    List<ProductImage> productImages;

    private String video;

    private long category_id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<VariantGroup> variantGroups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductVariant> productVariants;

    private long store_id;

    private Double rating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
