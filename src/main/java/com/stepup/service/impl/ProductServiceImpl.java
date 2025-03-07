package com.stepup.service.impl;

import com.stepup.entity.Product;
import com.stepup.entity.ProductImage;
import com.stepup.entity.ProductVariant;
import com.stepup.model.ProductVariantDTO;
import com.stepup.repository.ProductImageRepository;
import com.stepup.repository.ProductRepository;
import com.stepup.service.IProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    private ProductRepository repo;
    private ProductImageRepository imageRepo;

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return repo.findById(productId);
    }

    @Override
    public Optional<Product> getProductBySlug(String slug) {
        return repo.findBySlug(slug);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return repo.findByCategory_Id(categoryId);
    }

    @Override
    public List<Product> getActiveProducts() {
        return repo.findByIsActiveTrue();
    }

    @Override
    public List<Product> getInactiveProducts() {
        return repo.findByIsActiveFalse();
    }

    @Override
    public List<Product> searchProductsByName(String keyword) {
        return repo.searchByName(keyword);
    }

    @Override
    public String getCategoryNameByProductId(Long productId) {
        return repo.findCategoryNameByProductId(productId);
    }

    @Override
    public List<ProductVariant> getProductVariants(Long productId) {
        return repo.findVariantsByProductId(productId);
    }

    @Override
    public List<ProductImage> getProductImages(Long productId) {
        return imageRepo.findByProduct_Id(productId);
    }

    @Override
    public List<Product> getTopRatedProducts(Pageable pageable) {
        return repo.findTopRatedProducts(pageable);
    }

    @Override
    public List<Product> getLatestProducts(Pageable pageable) {
        return repo.findLatestProducts(pageable);
    }

    @Override
    public List<ProductVariantDTO> getProductsByCategoryAndPriceRange(Long categoryId, Double minPrice, Double maxPrice) {
        return repo.findByCategoryAndPriceRange(categoryId, minPrice, maxPrice);
    }

    @Override
    public Product saveProduct(Product product) {
        return repo.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        repo.deleteById(productId);
    }
}
