package com.stepup.controller;

import com.stepup.dtos.requests.ProductDTO;
import com.stepup.dtos.requests.ProductImageDTO;
import com.stepup.dtos.responses.ProductCardResponse;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Product;
import com.stepup.entity.ProductImage;
import com.stepup.mapper.IProductMapper;
import com.stepup.service.impl.ProductServiceImpl;
import com.stepup.utils.FileUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    IProductMapper productMapper;
    @Value("${maximum_per_product}")
    private int maximumPerProduct;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductCardResponse> productCardResponses = productMapper.toProductCard(products);
        return ResponseEntity.ok().body(productCardResponses);
    }

    @GetMapping("/{id}")
    public Product getProducts(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            return product;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO, BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message(errors.toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }
        try{
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Product created successfully")
                    .status(HttpStatus.OK)
                    .data(newProduct)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadProductImage(
            @PathVariable long id,
            @RequestParam("files") List<MultipartFile> files
    ) throws Exception {
        // Kiểm tra sản phẩm có tồn tại không
        Product existingProduct = productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        files = files == null ? new ArrayList<MultipartFile>() : files;
        // nếu số lượng ảnh vượt quá quy định thì báo lỗi
        if(files.size() > maximumPerProduct){
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message("Số lượng ảnh quá " + maximumPerProduct) // Dùng String Template
                            .build()
            );
        }
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if(file.getSize() == 0){
                continue;
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")){
                return ResponseEntity.badRequest().body(
                        ResponseObject.builder()
                                .message("không đúng định dạng ảnh")
                                .build()
                );
            }
            // Lưu file và cập nhật ảnh
            String fileName = FileUtils.storeFile(file);
            // lưu đối tượng product tỏng DB
            ProductImage productImage = productService.createProductImage(
                    existingProduct.getId(),
                    ProductImageDTO.builder()
                            .imageUrl(fileName).build()
            );
            productImages.add(productImage);
        }
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Upload image successfully")
                .status(HttpStatus.CREATED)
                .data(productImages)
                .build());
    }
}

