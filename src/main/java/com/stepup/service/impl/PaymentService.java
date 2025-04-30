package com.stepup.service.impl;

import com.stepup.entity.Order;
import com.stepup.entity.Payment;
import com.stepup.repository.OrderRepository;
import com.stepup.repository.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentService {
//    @Autowired
//    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    // Phương thức lưu kết quả giao dịch
//    private void saveTransactionResult(String vnp_TxnRef, String vnp_ResponseCode, String vnp_Amount, HttpServletRequest request) {
//           try {
//                    // Tìm đơn hàng liên quan dựa trên thông tin từ request hoặc database
//                    // Giả sử bạn lưu reference trong session hoặc database
//                    Long orderId = findOrderIdByTransactionRef(vnp_TxnRef);
//                    Order order = orderRepository.findById(orderId)
//                            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
//
//                    // Chuyển đổi amount (đã nhân với 100 khi tạo URL thanh toán)
//                    Long amount = Long.parseLong(vnp_Amount) / 100;
//
//                    // Xác định trạng thái giao dịch
//                    String transactionStatus = "00".equals(vnp_ResponseCode) ? "SUCCESS" : "FAILED";
//
//                    // Lấy thêm thông tin khác từ response nếu cần
//                    String vnp_BankCode = request.getParameter("vnp_BankCode");
//                    String vnp_CardType = request.getParameter("vnp_CardType");
//                    String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
//                    String vnp_PayDate = request.getParameter("vnp_PayDate");
//
//                    // Chuyển đổi format ngày từ VNPay (yyyyMMddHHmmss)
//                    LocalDateTime paymentTime = null;
//                    if (vnp_PayDate != null && !vnp_PayDate.isEmpty()) {
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//                paymentTime = LocalDateTime.parse(vnp_PayDate, formatter);
//            } else {
//                paymentTime = LocalDateTime.now();
//            }
//
//            // Tạo đối tượng giao dịch
//            Payment transaction = Payment.builder()
//                    .transactionRef(vnp_TxnRef)
//                    .amount(amount)
//                    .responseCode(vnp_ResponseCode)
//                    .transactionStatus(transactionStatus)
//                    .cardType(vnp_CardType)
//                    .orderInfo(vnp_OrderInfo)
//                    .paymentTime(paymentTime)
//                    .order(order)
//                    .build();
//
//            // Lưu giao dịch vào database
//            paymentRepository.save(transaction);
//
//            // Cập nhật trạng thái đơn hàng nếu thanh toán thành công
//            if ("00".equals(vnp_ResponseCode)) {
//                order.setPaymentStatus("PAID");
//                order.setStatus("PROCESSING"); // Hoặc trạng thái phù hợp với logic nghiệp vụ
//                orderRepository.save(order);
//            }
//
//        } catch (Exception e) {
//            // Xử lý ngoại lệ, ghi log
//            log.error("Lỗi khi lưu kết quả giao dịch: {}", e.getMessage(), e);
//        }
//    }
}
