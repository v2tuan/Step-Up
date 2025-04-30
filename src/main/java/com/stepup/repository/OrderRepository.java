package com.stepup.repository;

import com.stepup.Enum.OrderShippingStatus;
import com.stepup.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(Long userId);

    List<Order> findByAddress_Id(Long addressId);

    List<Order> findByUser_IdAndStatus(long id, OrderShippingStatus status);

    Optional<Order> findByOrderCode(String orderCode);

//    List<Order> findByPaymentStatus

    // Lấy danh sách đơn hàng theo trạng thái
//    List<Order> findByStatus(OrderShippingStatus status);

    // Tính tổng doanh thu của tất cả đơn hàng đã thanh toán
//    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.isPaidBefore = true")
//    Double getTotalRevenue();

    // Tính tổng doanh thu theo trạng thái đơn hàng
//    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = :status")
//    Double getRevenueByStatus(@Param("status") OrderShippingStatus status);
}
