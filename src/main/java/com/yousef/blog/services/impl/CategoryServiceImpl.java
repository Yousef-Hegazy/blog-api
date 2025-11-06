package com.yousef.blog.services.impl;

import com.yousef.blog.domain.entities.Category;
import com.yousef.blog.repositories.CategoryRepository;
import com.yousef.blog.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category entity) {
        if (categoryRepository.existsByNameIgnoreCase(entity.getName()))
            throw new IllegalArgumentException("Category already exists with name: " + entity.getName());

        return categoryRepository.save(entity);
    }
}
