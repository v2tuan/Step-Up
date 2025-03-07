package com.stepup.service;

import com.stepup.entity.Product;
import com.stepup.entity.VariantGroup;

import java.util.List;
import java.util.Optional;

public interface IVariantGroupService {
    //  Lấy danh sách nhóm biến thể theo productId
    List<VariantGroup> getVariantGroupsByProductId(Long productId);

    //  Lấy nhóm biến thể theo ID
    Optional<VariantGroup> getVariantGroupById(Long id);

    //  Lưu hoặc cập nhật nhóm biến thể
    VariantGroup saveVariantGroup(VariantGroup variantGroup);

    //  Xóa nhóm biến thể theo ID
    void deleteVariantGroup(Long id);

    // 5️⃣ Lấy danh sách sản phẩm theo nhóm biến thể (ID)
    List<Product> getProductsByVariantGroupId(Long variantGroupId);

}
