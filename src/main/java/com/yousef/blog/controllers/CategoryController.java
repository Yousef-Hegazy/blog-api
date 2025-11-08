package com.yousef.blog.controllers;

import com.yousef.blog.domain.dtos.CategoryDto;
import com.yousef.blog.domain.dtos.CreateCategoryRequest;
import com.yousef.blog.mappers.CategoryMapper;
import com.yousef.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest categoryDto) {
        var createdCategory = categoryService.createCategory(categoryMapper.toEntity(categoryDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toDto(createdCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
