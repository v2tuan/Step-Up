package com.stepup.service.impl;

import com.stepup.entity.ProductVariant;
import com.stepup.repository.ProductVariantRepository;
import com.stepup.service.IProductVariantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantServiceImpl implements IProductVariantService {
    private ProductVariantRepository repo;
    @Override
    public List<ProductVariant> getVariantsByProductId(Long productId) {
        return repo.findByProduct_Id(productId);
    }

    @Override
    public Optional<ProductVariant> getVariantById(Long variantId) {
        return repo.findById(variantId);
    }

    @Override
    public Optional<ProductVariant> getVariantBySku(String sku) {
        return repo.findBySku(sku);
    }

    @Override
    public ProductVariant saveVariant(ProductVariant productVariant) {
        return repo.save(productVariant);
    }

    @Override
    public void deleteVariant(Long variantId) {
        repo.deleteById(variantId);
    }
}
