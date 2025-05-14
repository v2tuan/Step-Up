package com.stepup.controller;

import com.stepup.Enum.OrderShippingStatus;
import com.stepup.Enum.PaymentStatus;
import com.stepup.components.VNPayUtils;
import com.stepup.dtos.requests.payment.PaymentDTO;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Order;
import com.stepup.service.impl.OrderServiceImpl;
import com.stepup.service.impl.PaymentService;
import com.stepup.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payments")
public class PaymentController {

    private final VNPayService vnPayService;

    @Autowired
    private VNPayUtils vnPayUtils;

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping("/create_payment_url")
    public ResponseEntity<ResponseObject> createPayment(@RequestBody PaymentDTO paymentRequest, HttpServletRequest request) {
        try {
            String paymentUrl = vnPayService.createPaymentUrl(paymentRequest, request);

            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Payment URL generated successfully.")
                    .data(paymentUrl)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message("Error generating payment URL: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/vnpay-return")
    public void handleVnPayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Xác thực và xử lý các tham số từ VNPAY
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_Amount = request.getParameter("vnp_Amount");
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        // Kiểm tra checksum
        boolean isValidChecksum = verifyChecksum(request);

        if (isValidChecksum) {
            // Lưu kết quả giao dịch vào database
//            saveTransactionResult(vnp_TxnRef, vnp_ResponseCode, vnp_Amount);
            orderService.updatePaymentStatus(vnp_TxnRef, PaymentStatus.COMPLETED);

            // Chuyển hướng về ứng dụng Android với thông tin giao dịch
            String redirectUrl = "yourapp://payment?txnRef=" + vnp_TxnRef +
                    "&responseCode=" + vnp_ResponseCode;
            response.sendRedirect(redirectUrl);
        } else {
            orderService.updatePaymentStatus(vnp_TxnRef, PaymentStatus.FAILED);
            // Xử lý trường hợp checksum không hợp lệ
            response.sendRedirect("yourapp://payment?error=invalid_checksum");

            throw new RuntimeException("Invalid checksum");
        }
    }

    // Phương thức kiểm tra checksum từ phản hồi của VNPay
    private boolean verifyChecksum(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();

        // Lấy tất cả tham số từ request
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);

            if (!paramName.equals("vnp_SecureHash") && !paramName.equals("vnp_SecureHashType")) {
                if (paramValue != null && !paramValue.isEmpty()) {
                    fields.put(paramName, paramValue);
                }
            }
        }

        // Lấy mã bảo mật từ response
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");

        // Tính toán mã bảo mật từ các tham số
        String calculatedHash = vnPayUtils.hashAllFields(fields);

        // So sánh mã bảo mật tính toán với mã bảo mật nhận được
        return calculatedHash.equals(vnp_SecureHash);
    }

    @PostMapping("/payment")
    public ResponseEntity<ResponseObject> Payment(@RequestParam Long orderId) {
        Optional<Order> optionalOrder = orderService.getOrderById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            orderService.updatePaymentStatus1(order,PaymentStatus.COMPLETED);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Payment order successfully")
                            .build()
            );
        }

        return ResponseEntity.badRequest().body(
                ResponseObject.builder()
                        .message("Payment the order failed")
                        .build()
        );
    }


}

