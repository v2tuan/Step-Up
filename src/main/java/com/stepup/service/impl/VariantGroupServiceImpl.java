package com.stepup.service.impl;

import com.stepup.entity.Product;
import com.stepup.entity.VariantGroup;
import com.stepup.repository.VariantGroupRepository;
import com.stepup.service.IVariantGroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VariantGroupServiceImpl implements IVariantGroupService  {
    private VariantGroupRepository repo;
    @Override
    public List<VariantGroup> getVariantGroupsByProductId(Long productId) {
        return repo.findByProduct_Id(productId);
    }

    @Override
    public Optional<VariantGroup> getVariantGroupById(Long id) {
        return repo.findById(id);
    }

    @Override
    public VariantGroup saveVariantGroup(VariantGroup variantGroup) {
        return repo.save(variantGroup);
    }

    @Override
    public void deleteVariantGroup(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Product> getProductsByVariantGroupId(Long variantGroupId) {
        return repo.findProductsByVariantGroupId(variantGroupId);
    }
}
