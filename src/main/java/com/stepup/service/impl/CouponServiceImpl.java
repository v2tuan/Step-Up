package com.stepup.service.impl;

import com.stepup.dtos.requests.CouponDTO;
import com.stepup.entity.Coupon;
import com.stepup.entity.CouponCondition;
import com.stepup.mapper.ICouponMapper;
import com.stepup.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private ICouponMapper couponMapper;

    public Coupon addCoupon(CouponDTO couponDTO) {
        Coupon coupon = couponMapper.toCoupon(couponDTO);
        for(CouponCondition condition : coupon.getCouponConditionList()){
            condition.setCoupon(coupon); // Set nhu th nay de luu id coupon vao CouponCondition
        }
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findValidExpiryCoupons();
        return coupons;
    }
}
