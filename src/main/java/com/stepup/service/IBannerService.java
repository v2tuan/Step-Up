package com.stepup.service;

import com.stepup.dtos.responses.BannerResponse;
import com.stepup.entity.Banner;

import java.util.List;

public interface IBannerService {
    // Lấy tất cả sản phẩm
    List<BannerResponse> getAllBanner();
}
