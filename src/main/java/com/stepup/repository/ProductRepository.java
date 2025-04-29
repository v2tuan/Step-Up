package com.stepup.repository;

import com.stepup.entity.Product;
import com.stepup.entity.ProductVariant;
import com.stepup.model.ProductVariantDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Id(Long categoryId);
    List<Product> findByIsActiveTrue();
    List<Product> findByIsActiveFalse();
    Optional<Product> findBySlug(String slug);

    // tìm kiếm sản phẩm theo tên
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);

    // Lấy tên danh mục của sản phẩm dựa vào productId
    @Query("SELECT p.category.name FROM Product p WHERE p.id = :productId")
    String findCategoryNameByProductId(@Param("productId") Long productId);

    // Tìm tất cả biến thể của sản phẩm
    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = :productId")
    List<ProductVariant> findVariantsByProductId(@Param("productId") Long productId);

    // Lọc sản phẩm có đánh giá cao nhất
    @Query("SELECT p FROM Product p ORDER BY p.rating DESC")
    List<Product> findTopRatedProducts(Pageable pageable);

    // Lấy sản phẩm mới nhất theo ngày tạo
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findLatestProducts(Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.isActive = true " +
            "AND (LOWER(p.category.name) LIKE CONCAT('%', :query, '%') " +
            "OR LOWER(p.name) LIKE CONCAT('%', :query, '%')) " +
            "ORDER BY " +
            "CASE " +
            "WHEN LOWER(p.category.name) LIKE CONCAT('%', :query, '%') THEN 2 " +
            "WHEN LOWER(p.name) LIKE CONCAT('%', :query, '%') THEN 1 " +
            "END")
    List<Product> findBySearchQuery(@Param("query") String query);

    @Query("SELECT p.name FROM Product p WHERE p.isActive = true")
    List<String> findAllActiveProductNames();
    // Tìm sản phẩm theo danh mục và khoảng giá
//    @Query("SELECT new com.stepup.model.ProductVariantDTO( " +
//            "p.id, p.name, p.slug, p.description, p.thumbnail, p.category.id, " +
//            "pv.id, pv.sku, pv.price, pv.promotionPrice, pv.quantity) " +
//            "FROM Product p " +
//            "LEFT JOIN p.productVariants pv " +
//            "WHERE p.category.id = :categoryId " +
//            "AND pv.price BETWEEN :minPrice AND :maxPrice ORDER BY pv.price ASC")
//    List<ProductVariantDTO> findByCategoryAndPriceRange(
//            @Param("categoryId") Long categoryId,
//            @Param("minPrice") Double minPrice,
//            @Param("maxPrice") Double maxPrice);


}
