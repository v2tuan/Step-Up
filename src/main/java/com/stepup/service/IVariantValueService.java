package com.stepup.service;

import com.stepup.entity.VariantValue;
import com.stepup.entity.Product;
import java.util.List;
import java.util.Optional;

public interface IVariantValueService {
    // Lấy danh sách giá trị biến thể theo nhóm biến thể
    List<VariantValue> getVariantValuesByGroupId(Long groupId);

    // Lấy giá trị biến thể theo ID
    Optional<VariantValue> getVariantValueById(Long id);

    // Lưu hoặc cập nhật giá trị biến thể
    VariantValue saveVariantValue(VariantValue variantValue);

    // Xóa giá trị biến thể theo ID
    void deleteVariantValue(Long id);

    // Lấy danh sách sản phẩm theo giá trị biến thể (ID)
    List<Product> getProductsByVariantValueId(Long variantValueId);
}
