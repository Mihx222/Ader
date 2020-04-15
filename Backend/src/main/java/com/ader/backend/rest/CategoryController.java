package com.ader.backend.rest;

import com.ader.backend.entity.category.Category;
import com.ader.backend.entity.category.CategoryDto;
import com.ader.backend.service.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<Object> getCategory(@PathVariable String name) {
        return categoryService.getCategory(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add")
    public ResponseEntity<Object> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Object> editCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("name/{name}")
    public ResponseEntity<Object> editCategory(@PathVariable String name, @RequestBody Category category) {
        return categoryService.updateCategory(name, category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("name/{name}")
    public ResponseEntity<Object> deleteCategory(@PathVariable String name) {
        return categoryService.deleteCategory(name);
    }
}
