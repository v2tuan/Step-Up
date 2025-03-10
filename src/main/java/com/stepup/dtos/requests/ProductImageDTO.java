package com.stepup.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageDTO {
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;

    @Size(min = 5, max = 200, message = "Image's name")
    private String imageUrl;
}
