package com.stepup.service;

import com.stepup.dtos.requests.ProductDTO;
import com.stepup.dtos.requests.ProductImageDTO;
import com.stepup.entity.Product;
import com.stepup.entity.ProductImage;
import com.stepup.entity.ProductVariant;
import com.stepup.model.ProductVariantDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    public Product createProduct(ProductDTO productDTO) throws Exception;

    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;

    // Lấy tất cả sản phẩm
    List<Product> getAllProducts();

    //  Lấy sản phẩm theo ID
    Optional<Product> getProductById(Long productId);

    //  Lấy sản phẩm theo Slug (SEO-friendly URL)
    Optional<Product> getProductBySlug(String slug);

    //  Lấy sản phẩm theo danh mục
    List<Product> getProductsByCategoryId(Long categoryId);

    //  Lấy danh sách sản phẩm đang hoạt động (có thể mua)
    List<Product> getActiveProducts();

    //  Lấy danh sách sản phẩm ngừng kinh doanh
    List<Product> getInactiveProducts();

    //  Tìm kiếm sản phẩm theo tên
    List<Product> searchProductsByName(String keyword);

    //  Lấy tên danh mục của sản phẩm
    String getCategoryNameByProductId(Long productId);

    //  Lấy danh sách biến thể của sản phẩm
    List<ProductVariant> getProductVariants(Long productId);

    // Lấy danh sách ảnh của sản phẩm
    List<ProductImage> getProductImages(Long productId);

    //  Lọc sản phẩm có đánh giá cao nhất
    List<Product> getTopRatedProducts(Pageable pageable);

    //  Lấy sản phẩm mới nhất theo ngày tạo
    List<Product> getLatestProducts(Pageable pageable);

    //  Tìm sản phẩm theo danh mục và khoảng giá
    List<ProductVariantDTO> getProductsByCategoryAndPriceRange(Long categoryId, Double minPrice, Double maxPrice);

    // Lưu hoặc cập nhật sản phẩm
    Product saveProduct(Product product);

    //  Xóa sản phẩm theo ID
    void deleteProduct(Long productId);
}
