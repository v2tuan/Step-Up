package com.stepup.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stepup.Enum.OrderShippingStatus;
import com.stepup.Enum.PaymentMethod;
import com.stepup.Enum.PaymentStatus;
import com.stepup.entity.Address;
import com.stepup.entity.Coupon;
import com.stepup.entity.OrderItem;
import com.stepup.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private long id;
    private String orderCode;
    private Address address;
    private double subTotal;
    private Double totalPrice; // = subTotal - discountPrice
    private Double discountPrice;
    private double shippingPrice = 0;
    private PaymentMethod paymentMethod;
    private List<OrderItemResponse> orderItems;
    private PaymentStatus paymentStatus;
    private OrderShippingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime  receiveDate;
}
