package com.yousef.blog.services;

import com.yousef.blog.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();

    Category getCategoryById(UUID id);

    Category createCategory(Category entity);

    void deleteCategory(UUID id);
}
