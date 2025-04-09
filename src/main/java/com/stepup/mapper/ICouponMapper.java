package com.stepup.mapper;

import com.stepup.dtos.requests.CouponDTO;
import com.stepup.entity.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICouponMapper {
    Coupon toCoupon(CouponDTO couponDTO);
}
