package com.stepup.service.impl;

import com.stepup.entity.Category;
import com.stepup.repository.CategoryRepository;
import com.stepup.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
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
    public Category findById(Long id) {
        return repo.findById(id).get();
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
}
