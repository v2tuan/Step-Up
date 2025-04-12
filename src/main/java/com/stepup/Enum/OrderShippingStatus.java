package com.stepup.Enum;

public enum OrderShippingStatus {
    PENDING,        // Đơn hàng chưa giao, đang chờ xử lý hoặc chuẩn bị
    DELIVERING,     // Đang giao hàng
    DELIVERED,      // Đã giao thành công
    CANCELED        // Đã huỷ
}
