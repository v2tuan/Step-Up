package com.stepup.service.impl;

import com.stepup.dtos.responses.BannerResponse;
import com.stepup.entity.Banner;
import com.stepup.mapper.IBannerMapper;
import com.stepup.repository.BannerRepository;
import com.stepup.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService implements IBannerService {
    @Autowired
    BannerRepository bannerRepository;
    @Autowired
    IBannerMapper bannerMapper;
    @Override
    public List<BannerResponse> getAllBanner() {
        List<Banner> banners = bannerRepository.findAll();
        return bannerMapper.toBannerResponseList(banners);
    }
}
