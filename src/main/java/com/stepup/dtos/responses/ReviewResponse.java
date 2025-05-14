package com.stepup.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private String userFullName;
    private String productName;
    private String content;
    private Integer rating;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}
