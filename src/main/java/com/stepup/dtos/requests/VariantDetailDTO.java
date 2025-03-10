package com.stepup.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class VariantDetailDTO {
    private List<String> variantValues; // Ví dụ: ["Đỏ", "S"]
    private Double price;
    private Double promotionPrice;
    private int quantity;
}
