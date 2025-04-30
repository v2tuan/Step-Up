package com.stepup.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stepup.dtos.requests.CouponDTO;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Coupon;
import com.stepup.service.impl.CouponServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/v1/coupon")
public class CouponController {
    @Autowired
    private CouponServiceImpl couponService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody CouponDTO couponDTO) {
        Coupon newCoupon = couponService.addCoupon(couponDTO);
        return ResponseEntity.ok(newCoupon);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllCoupon() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Lấy tất cả voucher")
                .data(coupons)
                .status(HttpStatus.OK)
                .build());
    }
}