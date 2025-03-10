package com.stepup.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class VariantGroupDTO {
    private String name;  // Ví dụ: "Màu sắc", "Kích thước"
    private List<String> values;  // Ví dụ: ["Đỏ", "Xanh"] hoặc ["S", "M", "L"]
}
