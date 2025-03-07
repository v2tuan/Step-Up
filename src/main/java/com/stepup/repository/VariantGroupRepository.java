package com.stepup.repository;

import com.stepup.entity.Product;
import com.stepup.entity.VariantGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantGroupRepository extends JpaRepository<VariantGroup, Long> {
    List<VariantGroup> findByProduct_Id(Long productId);

    // Lấy danh sách sản phẩm theo nhóm biến thể (ID)
    @Query("SELECT v.product FROM VariantGroup v WHERE v.id = :variantGroupId")
    List<Product> findProductsByVariantGroupId(@Param("variantGroupId") Long variantGroupId);

}
