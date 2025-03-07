package com.stepup.service;

import com.stepup.entity.Cart;
import com.stepup.entity.Category;
import java.util.List;
public interface ICategoryService {
    //  Lấy tất cả danh mục
    List<Category> getAllCategories();

    //  Lấy tên danh mục bằng ID
    String getCategoryNameById(Long categoryId);

    Category findById(Long id);

    long count();

    void deleteById(Long id);

    // Lưu hoặc cập nhật giỏ hàng
    Category saveCategory(Category cate);
}
