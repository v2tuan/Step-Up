package com.stepup.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {
    private Long oderId;

    @NotNull(message = "Product Variant ID is required")
    private Long productVariantId;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;

    @NotNull(message = "Count is required")
    @Min(value = 1, message = "Count must be at least 1")
    private Integer count;

    @NotNull(message = "Delivery ID is required")
    private Long deliveryId;
}
