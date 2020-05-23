package com.ader.backend.rest.actuator;

import com.ader.backend.entity.Category;
import com.ader.backend.rest.dto.CategoryDto;
import com.ader.backend.service.category.CategoryService;
import org.springframework.boot.actuate.endpoint.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Endpoint(id = "category")
public class CategoryEndpoint {

    private final CategoryService categoryService;

    public CategoryEndpoint(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ReadOperation
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(CategoryDto.toDto(categoryService.getAllCategories()));
    }

    @DeleteOperation
    public ResponseEntity<String> deleteCategory(@Selector String name) {
        return ResponseEntity.ok(categoryService.deleteCategory(name));
    }

    @WriteOperation
    public ResponseEntity<CategoryDto> createCategory(@Selector String name) {
        Category category = new Category();
        category.setName(name);
        return ResponseEntity.ok(CategoryDto.toDto(categoryService.createCategory(category)));
    }
}
