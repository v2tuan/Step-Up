package com.stepup.repository;


import com.stepup.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //  Lấy tất cả danh mục
    List<Category> findAll();

    // Lấy tên danh mục bằng ID
    @Query("SELECT c.name FROM Category c WHERE c.id = :categoryId")
    String findCategoryNameById(@Param("categoryId") Long categoryId);

    boolean existsByName(String name);
    @Query("SELECT c.name FROM Category c WHERE c.isDeleted = false")
    List<String> findAllCategoryNames();

}
