package com.stepup.mapper;

import com.stepup.dtos.responses.BannerResponse;
import com.stepup.entity.Banner;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IBannerMapper {
    BannerResponse toBannerResponse(Banner banner);
    List<BannerResponse> toBannerResponseList(List<Banner> banners);
}
