package com.stepup.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private boolean isActive;
    private boolean isSelling;

    private long categoryId;

    private String store_id;

    // Thông tin phân loại
    private List<VariantGroupDTO> variantGroups;

    // Thông tin giá và số lượng của từng biến thể
    private List<VariantDetailDTO> variants;
}