package com.stepup.dtos.requests;

import com.stepup.Enum.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponConditionDTO {
    private Attribute attribute;
    private String value;
}
