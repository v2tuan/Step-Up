package com.stepup.repository;

import com.stepup.entity.Product;
import com.stepup.entity.VariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantValueRepository extends JpaRepository<VariantValue, Long> {
    List<VariantValue> findByVariantGroup_Id(Long groupId);

    @Query("SELECT pv.product FROM ProductVariant pv JOIN pv.variantValues vv WHERE vv.id = :variantValueId")
    List<Product> findProductsByVariantValueId(@Param("variantValueId") Long variantValueId);


}
