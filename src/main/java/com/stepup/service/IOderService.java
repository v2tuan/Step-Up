package com.stepup.service;

import com.stepup.Enum.Status;
import com.stepup.dtos.requests.OrderDTO;
import com.stepup.entity.Order;

import java.util.List;
import java.util.Optional;
public interface IOderService {

    //  Lấy tất cả đơn hàng
    List<Order> getAllOrders();

    // Lấy đơn hàng theo ID
    Optional<Order> getOrderById(Long orderId);

    // Lấy danh sách đơn hàng theo người dùng
    List<Order> getOrdersByUserId(Long userId);

    //  Lấy danh sách đơn hàng theo trạng thái
    List<Order> getOrdersByStatus(Status status);

    //  Tạo mới hoặc cập nhật đơn hàng
    Order saveOrder(Order order);

    //  Cập nhật trạng thái đơn hàng
    Order updateOrderStatus(Long orderId, Status status);

    //  Xóa đơn hàng theo ID
    void deleteOrderById(Long orderId);

    // Tính tổng doanh thu của tất cả đơn hàng đã thanh toán
    Double getTotalRevenue();

    //  Tính tổng doanh thu theo trạng thái đơn hàng
    Double getRevenueByStatus(Status status);

    Order createOrder(OrderDTO orderDTO);
}
