package com.stepup.dtos.responses;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerResponse {
    private String title;

    @Column(name = "image_url")
    private String imageUrl;
}
