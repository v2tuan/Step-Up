package com.stepup.dtos.responses;

import com.stepup.entity.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemRespone {
    private ProductVariant productVariant;
    private int count;
}
