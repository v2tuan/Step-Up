package com.stepup.dtos.requests.payment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {

//    @JsonProperty("amount")
//    private Long amount; // Số tiền cần thanh toán

    @JsonProperty("orderId")
    private Long orderId; // Số tiền cần thanh toán

    @JsonProperty("bankCode")
    private String bankCode;
    /// Mã phương thức thanh toán, mã loại ngân hàng hoặc ví điện tử thanh toán.
    /// Nếu không gửi sang tham số này, chuyển hướng người dùng sang VNPAY chọn phương thức thanh toán.

    @JsonProperty("language")
    private String language; // Ngôn ngữ giao diện thanh toán (vd: "vn", "en")
}
