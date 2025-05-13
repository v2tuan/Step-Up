package com.stepup.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    @NotNull(message = "productId không được để trống")
    private Long productVariantId;

    @NotNull(message = "orderId không được để trống")
    private Long orderId;

    @Size(max = 1000, message = "Nội dung không được vượt quá 1000 ký tự")
    private String content;

    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải từ 1 đến 5")
    @Max(value = 5, message = "Rating phải từ 1 đến 5")
    private Integer rating;

    @Size(max = 5, message = "Không thể tải lên quá 5 ảnh")
    private List<MultipartFile> images;
}
