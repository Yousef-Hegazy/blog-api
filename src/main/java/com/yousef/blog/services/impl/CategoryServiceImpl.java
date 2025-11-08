package com.yousef.blog.services.impl;

import com.yousef.blog.domain.entities.Category;
import com.yousef.blog.repositories.CategoryRepository;
import com.yousef.blog.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category does not exist with id " + id)
                );
    }

    @Override
    @Transactional
    public Category createCategory(Category entity) {
        if (categoryRepository.existsByNameIgnoreCase(entity.getName()))
            throw new IllegalArgumentException("Category already exists with name: " + entity.getName());

        return categoryRepository.save(entity);
    }

    @Override
    public void deleteCategory(UUID id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        if (!category.getPosts().isEmpty())
            throw new IllegalStateException("Cannot delete category with associated posts.");

        categoryRepository.delete(category);
    }
}
