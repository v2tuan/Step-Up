package com.stepup.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {
    public String orderId;
    public String productVariantId;
    // giá ở đây dựa trên số lượng product và coupons
    private double Price;
    private int count;
    public String delivery_id;
}
