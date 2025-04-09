package com.stepup.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stepup.Enum.Attribute;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "coupon_conditions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    @JsonBackReference
    private Coupon coupon;

    @Column(name = "attribute", nullable = false)
    @Enumerated(EnumType.STRING) // Lưu giá trị enum dưới dạng chuỗi trong cơ sở dữ liệu.
    private Attribute attribute;

    @Column(name = "value", nullable = false)
    private String value;
}