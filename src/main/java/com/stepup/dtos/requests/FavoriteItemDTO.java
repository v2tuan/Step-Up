package com.stepup.dtos.requests;

import com.stepup.entity.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteItemDTO {
    private long productVariantId;
}
