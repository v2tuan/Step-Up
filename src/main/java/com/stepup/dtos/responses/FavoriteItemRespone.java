package com.stepup.dtos.responses;

import com.stepup.entity.ProductVariant;
import lombok.Data;

@Data
public class FavoriteItemRespone {
    private long id;
    private String title;
    private ProductVariant productVariant;
}
