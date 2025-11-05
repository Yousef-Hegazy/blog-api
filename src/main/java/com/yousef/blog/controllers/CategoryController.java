package com.yousef.blog.controllers;

import com.yousef.blog.domain.dtos.CategoryDto;
import com.yousef.blog.mappers.CategoryMapper;
import com.yousef.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories() {
        return ResponseEntity.ok(
                categoryService
                        .listCategories()
                        .stream()
                        .map(categoryMapper::toDto)
                        .toList()
        );
    }
}
