package com.stepup.service.impl;

import com.stepup.entity.Product;
import com.stepup.entity.VariantValue;
import com.stepup.repository.VariantValueRepository;
import com.stepup.service.IVariantValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VariantValueServiceImpl implements IVariantValueService {
    private VariantValueRepository repo;
    @Override
    public List<VariantValue> getVariantValuesByGroupId(Long groupId) {
        return repo.findByVariantGroup_Id(groupId);
    }

    @Override
    public Optional<VariantValue> getVariantValueById(Long id) {
        return repo.findById(id);
    }

    @Override
    public VariantValue saveVariantValue(VariantValue variantValue) {
        return repo.save(variantValue);
    }

    @Override
    public void deleteVariantValue(Long id) {
            repo.deleteById(id);
    }

    @Override
    public List<Product> getProductsByVariantValueId(Long variantValueId) {
        return repo.findProductsByVariantValueId(variantValueId);
    }
}
