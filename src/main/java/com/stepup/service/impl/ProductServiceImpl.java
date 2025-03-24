package com.stepup.service.impl;

import com.stepup.dtos.requests.*;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.*;
import com.stepup.mapper.IProductMapper;
import com.stepup.model.ProductVariantDTO;
import com.stepup.repository.CategoryRepository;
import com.stepup.repository.ColorRepository;
import com.stepup.repository.ProductImageRepository;
import com.stepup.repository.ProductRepository;
import com.stepup.service.IProductService;
import com.stepup.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private ProductImageRepository imageRepo;
    @Autowired
    ProductImageRepository productImageRespository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    IProductMapper productMapper;
    @Autowired
    ColorRepository colorRepository;

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

        // 2. Tạo các nhóm phân loại màu sắc (Color)
        Map<String, Color> valueMapColor = new HashMap<>();
        List<Color> colors = new ArrayList<>();

        for (String colorItem : productDTO.getColors()) {
            Color color = new Color();
            color.setName(colorItem);
            color.setProduct(product);
            valueMapColor.put(colorItem, color);
            colors.add(color);
        }
        product.setColors(colors);

        // 3. Tạo các nhóm phân loại kích thước (Size)
        Map<String, Size> valueMapSize = new HashMap<>();
        List<Size> sizes = new ArrayList<>();

        for (String sizeItem : productDTO.getSizes()) {
            Size size = new Size();
            size.setName(sizeItem);
            size.setProduct(product);
            valueMapSize.put(sizeItem, size);
            sizes.add(size);
        }
        product.setSizes(sizes);

        // 4. Tạo các biến thể (ProductVariant)
        List<ProductVariant> productVariants = new ArrayList<>();

        for (VariantDetailDTO variantDTO : productDTO.getVariants()) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setQuantity(variantDTO.getQuantity());

            // Lấy màu sắc từ variantDTO
            String colorName = variantDTO.getColor();
            Color color = valueMapColor.get(colorName);
            variant.setColor(color);

            // Lấy kích thước từ variantDTO
            String sizeName = variantDTO.getSize();
            Size size = valueMapSize.get(sizeName);
            variant.setSize(size);

            productVariants.add(variant);
        }
        product.setProductVariants(productVariants);

        // 5. Lưu sản phẩm vào cơ sở dữ liệu
        return repo.save(product);
    }

//    @Override
//    public Product updateProductColorImage(long id, ColorListDTO colorDTOList) throws Exception {
//        // Kiểm tra sản phẩm có tồn tại không
//        Product existingProduct = getProductById(id).orElseThrow(() -> new RuntimeException("Product not found"));
//
//        List<Color> colors = new ArrayList<>();
//        for(ColorDTO colorItem : colorDTOList.getColors()) {
//            Color color = colorRepository.findById(colorItem.getId()).orElseThrow(() -> new RuntimeException("Color not found"));
//            List<ColorImage> colorImageList = new ArrayList<>();
//            for (MultipartFile file : colorItem.getColorImages()) {
//                if(file.getSize() == 0){
//                    continue;
//                }
//                ColorImage colorImage = new ColorImage();
//                String contentType = file.getContentType();
//                if(contentType == null || !contentType.startsWith("image/")){
//                    throw new RuntimeException("Ảnh không đúng định dạng");
//                }
//                // Lưu file và cập nhật ảnh
//                String fileName = FileUtils.storeFile(file);
//                colorImage.setColor(color);
//                colorImage.setImageUrl(fileName);
//                colorImageList.add(colorImage);
//            }
//            color.setColorImages(colorImageList);
//        }
//
//        existingProduct.setColors(colors);
//        return repo.save(existingProduct);
//    }

    // Tối ưu hiệu suất
    @Override
    public Product updateProductColorImage(long id, ColorListDTO colorDTOList) {
        Product existingProduct = getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Color> updatedColors = new ArrayList<>();

        for (ColorDTO colorItem : colorDTOList.getColors()) {
            Color color = colorRepository.findById(colorItem.getId())
                    .orElseThrow(() -> new RuntimeException("Color not found"));

            List<ColorImage> colorImageList = new ArrayList<>();

            colorImageList = colorItem.getColorImages()
                    .parallelStream()
                    .filter(file -> file != null && file.getSize() > 0)
                    .map(file -> {
                        String contentType = file.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                            throw new RuntimeException("Ảnh không đúng định dạng: " + file.getOriginalFilename());
                        }

                        try {
                            String fileName = FileUtils.storeFile(file);  // Cần xử lý exception tại đây
                            ColorImage colorImage = new ColorImage();
                            colorImage.setColor(color);
                            colorImage.setImageUrl(fileName);
                            return colorImage;
                        } catch (Exception e) {
                            // Bạn có thể log hoặc báo lỗi rõ ràng hơn
                            throw new RuntimeException("Lỗi khi lưu ảnh: " + file.getOriginalFilename(), e);
                        }
                    })
                    .collect(Collectors.toList());

            color.setColorImages(colorImageList);
            updatedColors.add(color);
        }

        existingProduct.setColors(updatedColors);
        return repo.save(existingProduct);
    }




//    @Override
//    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
//        Product existingProduct = repo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
//        ProductImage productImage = new ProductImage();
//        ProductImage newProductImage = ProductImage.builder()
//                .imageUrl(productImageDTO.getImageUrl())
////                .product(existingProduct)
//                .build();
//
//        if (existingProduct.getThumbnail() == null ) {
//            existingProduct.setThumbnail(newProductImage.getImageUrl());
//        }
//        int size = productImageRespository.findByProduct_Id(productId).size();
//        if(size > maximumPerProduct){
//            throw new RuntimeException(
//                    "Number of images must be <= "
//                            + maximumPerProduct);
//        }
//
//        repo.save(existingProduct);
//        return productImageRespository.save(newProductImage);
//    }

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

//    @Override
//    public List<ProductImage> getProductImages(Long productId) {
//        return imageRepo.findByProduct_Id(productId);
//    }

    @Override
    public List<Product> getTopRatedProducts(Pageable pageable) {
        return repo.findTopRatedProducts(pageable);
    }

    @Override
    public List<Product> getLatestProducts(Pageable pageable) {
        return repo.findLatestProducts(pageable);
    }

//    @Override
//    public List<ProductVariantDTO> getProductsByCategoryAndPriceRange(Long categoryId, Double minPrice, Double maxPrice) {
//        return repo.findByCategoryAndPriceRange(categoryId, minPrice, maxPrice);
//    }

    @Override
    public Product saveProduct(Product product) {
        return repo.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        repo.deleteById(productId);
    }
}
