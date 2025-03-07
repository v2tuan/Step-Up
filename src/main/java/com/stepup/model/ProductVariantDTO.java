package com.stepup.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {
    private Long productId;
    private String productName;
    private String productSlug;
    private String description;
    private String thumbnail;
    private Long categoryId;
    private Long variantId;
    private String sku;
    private Double price;
    private Double promotionPrice;
    private Integer quantity;
}
