package com.stepup.service;

import com.stepup.entity.ProductVariant;

import java.util.List;
import java.util.Optional;

public interface IProductVariantService {
    //  Lấy danh sách biến thể của sản phẩm theo productId
    List<ProductVariant> getVariantsByProductId(Long productId);

    //  Lấy biến thể theo ID
    Optional<ProductVariant> getVariantById(Long variantId);

    //  Thêm hoặc cập nhật biến thể
    ProductVariant saveVariant(ProductVariant productVariant);

    //  Xóa biến thể theo ID
    void deleteVariant(Long variantId);
}
