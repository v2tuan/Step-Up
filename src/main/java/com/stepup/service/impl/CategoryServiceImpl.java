package com.stepup.service.impl;

import com.stepup.dtos.requests.CategoryDTO;
import com.stepup.entity.Category;
import com.stepup.entity.Product;
import com.stepup.repository.CategoryRepository;
import com.stepup.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryRepository repo;
    @Override
    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    @Override
    public String getCategoryNameById(Long categoryId) {
        return repo.findCategoryNameById(categoryId);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Category saveCategory(Category cate) {
        return repo.save(cate);
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        if (repo.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("Category already exists!");
        }

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .slug(categoryDTO.getSlug())
                .image(categoryDTO.getImage())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return repo.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        category.setName(categoryDTO.getName());
        category.setImage( categoryDTO.getImage());
        category.setSlug(categoryDTO.getSlug());
        category.setUpdatedAt(LocalDateTime.now());
        return repo.save(category);
    }
}
