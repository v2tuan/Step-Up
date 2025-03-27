package com.stepup.dtos.requests;

import com.stepup.Enum.Status;
import com.stepup.entity.Address;
import com.stepup.entity.User;
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
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Address ID is required")
    private Long addressId;

    @Builder.Default
    private Status status = Status.PENDING;

    @NotNull(message = "Payment status is required")
    private Boolean isPaidBefore;

    @NotNull(message = "Total price cannot be null")
    private Double totalPrice;

    @Valid
    @NotNull(message = "Order items cannot be null")
    private List<OrderItemDTO> orderItems;
}
