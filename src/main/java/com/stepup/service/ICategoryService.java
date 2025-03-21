package com.stepup.service;

import com.stepup.dtos.requests.CategoryDTO;
import com.stepup.entity.Cart;
import com.stepup.entity.Category;
import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    //  Lấy tất cả danh mục
    List<Category> getAllCategories();

    //  Lấy tên danh mục bằng ID
    String getCategoryNameById(Long categoryId);

    Optional<Category> findById(Long id);

    long count();

    void deleteById(Long id);

    // Lưu hoặc cập nhật giỏ hàng
    Category saveCategory(Category cate);

    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(Long id, CategoryDTO categoryDTO);
}
