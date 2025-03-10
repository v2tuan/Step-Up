package com.stepup.service.impl;

import com.stepup.dtos.requests.ProductDTO;
import com.stepup.dtos.requests.ProductImageDTO;
import com.stepup.dtos.requests.VariantDetailDTO;
import com.stepup.dtos.requests.VariantGroupDTO;
import com.stepup.entity.*;
import com.stepup.mapper.IProductMapper;
import com.stepup.model.ProductVariantDTO;
import com.stepup.repository.CategoryRepository;
import com.stepup.repository.ProductImageRepository;
import com.stepup.repository.ProductRepository;
import com.stepup.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    private ProductRepository repo;
    private ProductImageRepository imageRepo;
    @Autowired
    ProductImageRepository productImageRespository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    IProductMapper productMapper;

    @Value("${maximum_per_product}")
    private int maximumPerProduct;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws Exception {
        // 1. Tạo sản phẩm cơ bản
        Product product = productMapper.toProduct(productDTO);
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new Exception("Category not found"));
        product.setCategory(category);

        // 2. Tạo các nhóm phân loại và giá trị
        Map<String, VariantValue> valueMap = new HashMap<>();
        List<VariantGroup> VariantGroups = new ArrayList<>();

        for(VariantGroupDTO groupDTO : productDTO.getVariantGroups()){
            VariantGroup group = new VariantGroup();
            group.setName(groupDTO.getName());
            group.setProduct(product);

            List<VariantValue> variantValues = new ArrayList<>();
            for (String valueName : groupDTO.getValues()) {
                VariantValue value = new VariantValue();
                value.setName(valueName);
                value.setVariantGroup(group);
                variantValues.add(value); // Thêm vào danh sách
                valueMap.put(valueName, value);
            }

            group.setVariants(variantValues); // ✅ Cập nhật danh sách bằng `setVariantValues()`
            VariantGroups.add(group);
        }
        product.setVariantGroups(VariantGroups);

        List<ProductVariant> productVariants = new ArrayList<>();
        // 3. Tạo các biến thể với giá và số lượng
        for (VariantDetailDTO variantDTO : productDTO.getVariants()) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setPrice(variantDTO.getPrice());
            variant.setQuantity(variantDTO.getQuantity());

            // Thêm các giá trị phân loại cho biến thể
            List<VariantValue> variantValues = variantDTO.getVariantValues().stream()
                    .map(valueMap::get)
                    .collect(Collectors.toList());
            // tạo luồng ví dụ variantDTO.getVariantValues() là các string ví dụ là xanh , M thì tạo ra 1 luồng so sánh
            // với map nó sẽ sữa vào đó ánh xạ cho ra VariantValue sau đó tạo thành 1 list lưu vào variantValues
            variant.setVariantValues(variantValues);

            // Tạo SKU
//            String sku = generateSku(product.getName(), variantValues);
//            variant.setSku(sku);

            productVariants.add(variant);
        }
        product.setProductVariants(productVariants);
        return repo.save(product);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = repo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        ProductImage productImage = new ProductImage();
        ProductImage newProductImage = ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .product(existingProduct)
                .build();

        if (existingProduct.getThumbnail() == null ) {
            existingProduct.setThumbnail(newProductImage.getImageUrl());
        }
        int size = productImageRespository.findByProduct_Id(productId).size();
        if(size > maximumPerProduct){
            throw new RuntimeException(
                    "Number of images must be <= "
                            + maximumPerProduct);
        }

        repo.save(existingProduct);
        return productImageRespository.save(newProductImage);
    }

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
