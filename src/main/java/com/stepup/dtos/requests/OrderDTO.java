package com.stepup.dtos.requests;

import com.stepup.Enum.OrderShippingStatus;
import com.stepup.Enum.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class OrderDTO {
    @NotNull(message = "Address ID is required")
    private Long addressId;

    private Long couponId;

    @NotNull(message = "Payment Method ID is required")
    private PaymentMethod paymentMethod;

    @Valid
    @NotNull(message = "Order items cannot be null")
    private List<OrderItemDTO> orderItems;
}
