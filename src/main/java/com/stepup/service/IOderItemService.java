package com.stepup.service;

import com.stepup.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface IOderItemService {

    //  Lấy tất cả sản phẩm trong một đơn hàng
    List<OrderItem> getOrderItemsByOrderId(Long orderId);

    //  Lấy sản phẩm trong đơn hàng theo ID
    Optional<OrderItem> getOrderItemById(Long orderItemId);

    //  Thêm hoặc cập nhật sản phẩm trong đơn hàng
    OrderItem saveOrderItem(OrderItem orderItem);

    //  Xóa sản phẩm trong đơn hàng theo ID
    void deleteOrderItem(Long orderItemId);

    // Xóa tất cả sản phẩm trong một đơn hàng
    void deleteOrderItemsByOrderId(Long orderId);

    //  Tính tổng tiền của đơn hàng (không tính khuyến mãi)
    Double getTotalPriceByOrderId(Long orderId);

    //  Tính tổng tiền của đơn hàng (có tính khuyến mãi)
    Double getTotalDiscountedPriceByOrderId(Long orderId);
}
