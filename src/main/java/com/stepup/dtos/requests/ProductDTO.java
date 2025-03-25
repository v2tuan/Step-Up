package com.stepup.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private boolean isActive;

    private Double price;
    private Double promotionPrice;

    private long categoryId;

    // Thông tin phân loại
    private List<String> colors;

    private List<String> sizes;

    // Thông tin giá và số lượng của từng biến thể
    private List<VariantDetailDTO> variants;
}