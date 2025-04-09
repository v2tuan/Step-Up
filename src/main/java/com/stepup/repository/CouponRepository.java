package com.stepup.repository;

import com.stepup.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByActiveIsTrue();

    @Query(value = """
    SELECT coupons.* FROM coupon_conditions join  coupons
        ON coupon_conditions.coupon_id = coupons.id AND attribute = 'EXPIRY'
        AND STR_TO_DATE(value, '%Y-%m-%d') > CURDATE()
        AND active = 1
    """, nativeQuery = true)
    List<Coupon> findValidExpiryCoupons();

}
